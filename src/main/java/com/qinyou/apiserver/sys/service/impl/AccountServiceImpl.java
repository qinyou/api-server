package com.qinyou.apiserver.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qinyou.apiserver.core.base.RequestException;
import com.qinyou.apiserver.core.base.ResultEnum;
import com.qinyou.apiserver.core.component.JwtService;
import com.qinyou.apiserver.core.component.MailService;
import com.qinyou.apiserver.core.component.SmsService;
import com.qinyou.apiserver.core.utils.DateUtils;
import com.qinyou.apiserver.core.utils.WebUtils;
import com.qinyou.apiserver.sys.dto.UserInfoForm;
import com.qinyou.apiserver.sys.dto.ChangePwdForm;
import com.qinyou.apiserver.sys.dto.ResetPwdForm;
import com.qinyou.apiserver.sys.dto.UserInfo;
import com.qinyou.apiserver.sys.entity.User;
import com.qinyou.apiserver.sys.entity.VerificationCode;
import com.qinyou.apiserver.sys.mapper.UserMapper;
import com.qinyou.apiserver.sys.service.IAccountService;
import com.qinyou.apiserver.sys.service.IUserService;
import com.qinyou.apiserver.sys.service.IVerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {
    // 验证码最长有效期
    @Value("${app.safe-code.max-duration}")
    Integer maxDuration;
    // 重置密码 验证码邮件主题
    @Value("${app.safe-code.email-subject}")
    String emailSubject;
    // 同账号 同目的 邮件发送最小频率，单位分钟, 限制账号高频率发邮件
    @Value("${app.safe-code.min-rate}")
    Integer minRate = 1;
    // 同账号短信验证码 每天最大发送数量
    @Value("${app.safe-code.phone-max-number}")
    Integer maxNumber;

    @Autowired
    UserMapper userMapper;
    @Autowired
    IUserService userService;
    @Autowired
    IVerificationCodeService verificationCodeService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Qualifier("jwtUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtService jwtService;
    @Autowired
    MailService mailService;
    @Autowired
    SmsService smsService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generate(username);
    }

    @Override
    public UserInfo getUserInfo(String username) {
        UserInfo userInfo = new UserInfo();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", username));
        if (user == null) {
            throw RequestException.fail(ResultEnum.USERNAME_NOT_FOUND, new Object[]{username});
        }

        BeanUtils.copyProperties(user, userInfo);
        userInfo.setUsername(user.getId());
        userInfo.setRoles(userMapper.getUserRoles(username));
        userInfo.setResources(userMapper.getUserResources(username));
        return userInfo;
    }

    @Override
    public void updateUserInfo(String username, UserInfoForm userInfoForm) {
        User user = Optional.of(userService.getById(username)).get();
        if (StrUtil.isNotBlank(userInfoForm.getPhone()) && userService.checkExist(username, userInfoForm.getPhone(), null)) {
            throw RequestException.fail(ResultEnum.PHONE_EXIST);
        }
        if (StrUtil.isNotBlank(userInfoForm.getEmail()) && userService.checkExist(username, null, userInfoForm.getEmail())) {
            throw RequestException.fail(ResultEnum.EMAIL_EXIST);
        }
        BeanUtils.copyProperties(userInfoForm, user);
        user.setUpdateTime(LocalDateTime.now()).setUpdater(WebUtils.getSecurityUsername());
        userService.updateById(user);
    }

    @Override
    public void sendMailCode(String email) {
        User user = userService.getOne(new QueryWrapper<>(new User().setEmail(email)));
        if (user == null) {
            throw RequestException.fail(ResultEnum.EMAIL_NOT_FOUND);
        }

        //  限制相同目的、高频率 同账号 多次发送邮件
        List<VerificationCode> verificationCodes = verificationCodeService.list(
                new QueryWrapper<VerificationCode>()
                        .ge("create_time", DateUtils.formatDateTime(LocalDateTime.now().minusDays(1)))
                        .eq("account", email)
                        .eq("purpose", "0")
                        .orderBy(true, false, "create_time")
        );
        if (verificationCodes.size() > 0) {
            VerificationCode verificationCode = verificationCodes.get(0);
            if (Duration.between(verificationCode.getCreateTime(), LocalDateTime.now()).toMinutes() < minRate) {
                log.debug("间隔小于{}分钟，发送邮件失败", minRate);
                throw RequestException.fail(ResultEnum.FAIL);
            }
        }

        String code = RandomUtil.randomNumbers(6);
        boolean flag = mailService.sendTextMail(email, emailSubject, code);
        if (!flag) {
            throw RequestException.fail(ResultEnum.FAIL);
        }
        // 存库
        addVerificationCode(email, code);
    }

    @Override
    public void sendPhoneCode(String phone) {
        User user = userService.getOne(new QueryWrapper<>(new User().setPhone(phone)));
        if (user == null) {
            throw RequestException.fail(ResultEnum.PHONE_NOT_FOUND);
        }
        //  限制 同目的、同账号高频率 多次发送短信验证码
        String beginOfToday = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyyy-MM-dd");
        List<VerificationCode> verificationCodes = verificationCodeService.list(
                new QueryWrapper<VerificationCode>()
                        .ge("create_time", beginOfToday)
                        .eq("account", phone)
                        .eq("purpose", "0")
                        .orderBy(true, false, "create_time")
        );
        if (verificationCodes.size() > maxNumber) {
            log.debug("发送短信验证码失败, {} 发送数量大于 {}");
            throw RequestException.fail(ResultEnum.FAIL);
        }
        if (verificationCodes.size() > 0) {
            VerificationCode verificationCode = verificationCodes.get(0);
            if (Duration.between(verificationCode.getCreateTime(), LocalDateTime.now()).toMinutes() < minRate) {
                log.debug("间隔小于{}分钟，发送短信失败", minRate);
                throw RequestException.fail(ResultEnum.FAIL);
            }
        }

        String code = RandomUtil.randomNumbers(6);
        boolean flag = smsService.send(phone, code);
        if (!flag) {
            throw RequestException.fail(ResultEnum.FAIL);
        }
        addVerificationCode(phone, code);
    }

    @Transactional
    @Override
    public void resetPwd(ResetPwdForm resetPwdForm) {
        List<VerificationCode> verificationCodes = verificationCodeService.list(
                new QueryWrapper<VerificationCode>()
                        .eq("code", resetPwdForm.getCode())
                        .eq("account", resetPwdForm.getAccount())
                        .eq("purpose", "0")  // 0 代表用途重置密码
                        .eq("state", "0")    // 0 代表 未使用
                        .ge("create_time", LocalDateTime.now().minusDays(1))
                        .orderBy(true, false, "create_time")
        );

        // 找不到数据
        if (verificationCodes.size() == 0) {
            log.debug("1天内不存在验证码，account {} : code {} : purpose 0", resetPwdForm.getAccount(), resetPwdForm.getCode());
            throw RequestException.fail(ResultEnum.CODE_INVALID);
        }
        VerificationCode verificationCode = verificationCodes.get(0);

        // 过期
        if (Duration.between(verificationCode.getCreateTime(), LocalDateTime.now()).toMinutes() >= maxDuration) {
            log.debug("验证码过期，account {} : code {} : purpose 0", resetPwdForm.getAccount(), resetPwdForm.getCode());
            throw RequestException.fail(ResultEnum.CODE_INVALID);
        }

        User user = userService.getOne(
                new QueryWrapper<User>()
                        .eq("email", resetPwdForm.getAccount())
                        .or()
                        .eq("phone", resetPwdForm.getAccount())
        );
        if (user == null) {
            throw RequestException.fail(ResultEnum.ACCOUNT_NOT_EXIST);
        }
        user.setPassword(passwordEncoder.encode(resetPwdForm.getPassword()))
                .setUpdateTime(LocalDateTime.now()).setUpdater(user.getId());
        userService.updateById(user);
        verificationCode.setState("1");
        verificationCodeService.updateById(verificationCode);
    }

    @Override
    public void changePwd(String username, ChangePwdForm changePwdForm) {
        User user = Optional.of(userService.getById(username)).get();
        if (!passwordEncoder.matches(changePwdForm.getOldPwd(), user.getPassword())) {
            log.info("用户{} 密码 {} 错误", username, changePwdForm.getOldPwd());
            throw RequestException.fail(ResultEnum.FAIL);
        }
        String hashPwd = passwordEncoder.encode(changePwdForm.getNewPwd());
        userService.update(
                new UpdateWrapper<User>()
                        .set("password", hashPwd)
                        .eq("id", user.getId())
        );
    }


    /**
     * 发送验证码成功后，验证码存库
     *
     * @param account
     * @param code
     */
    private void addVerificationCode(String account, String code) {
        VerificationCode verificationCode = new VerificationCode()
                .setAccount(account)
                .setState("0")
                .setPurpose("0")
                .setCode(code)
                .setCreateTime(LocalDateTime.now());
        verificationCodeService.saveOrUpdate(verificationCode);
    }
}

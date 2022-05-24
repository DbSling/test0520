package com.sling.test0520.web;

import com.sling.test0520.domain.User;
import com.sling.test0520.service.UserService;
import com.sling.test0520.support.security.SecurityInterceptor;
import com.sling.test0520.web.dto.Login;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
//@SessionAttributes("login")
@RequiredArgsConstructor
public class UserWeb {
    private static final String VIEW_NAME = "login/loginHome";
    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userSvc;

    @GetMapping("/")
    public String userInfoMatch(@RequestParam(required = false) String userData, Model md) {
        md.addAttribute("login", new User());
        if (userData != null) {
            md.addAttribute("notlogin", true);
        }
        return VIEW_NAME;
    }

    @PostMapping("/")
    public String loginSubmit(@ModelAttribute @Valid Login user, BindingResult errors, SessionStatus state, HttpServletRequest req, HttpServletResponse res)
            throws UnsupportedEncodingException {
       logger.info("user::{}", user);
        if (ObjectUtils.isEmpty(user))  {
            errors.reject("login", "이메일 또는 패스워드가 정확하지 않습니다");
            return VIEW_NAME;
        }

        User userInfo = userSvc.userLogin(user);

        if (userInfo == null) {
            errors.reject("login", "이메일 또는 패스워드가 정확하지 않습니다");
            return VIEW_NAME;
        } else {
            state.setComplete();
            Cookie idCookie = new Cookie(SecurityInterceptor.ID_COOKIE_NAME, "" + userInfo.getId());
            Cookie nameCookie = new Cookie(SecurityInterceptor.NAME_COOKIE_NAME, URLEncoder.encode(userInfo.getName(), "utf-8"));
            Cookie ipCookie = new Cookie(SecurityInterceptor.IP_COOKIE_NAME, req.getRemoteAddr());
            logger.info("q1w2e3" + req.getRemoteUser() + ", " + req.getRequestedSessionId() + ", " + req.getRequestURI() + ", " + req.getRemoteHost());
            idCookie.setPath("/");
            nameCookie.setPath("/");
            ipCookie.setPath("/");
            logger.info("w2e3r4" + idCookie);
            res.addCookie(idCookie);
            res.addCookie(nameCookie);
            res.addCookie(ipCookie);

            logger.debug("Logged in : " + userInfo.toString() + " / " + req.getRemoteAddr());

            return "login/home";
        }
    }

    //@GetMapping("/home")
    //public String home() {
    //    return "home";
    //}

    @GetMapping("/join")
    public String join() {
        return "login/join";
    }

    @PostMapping("/saveUser")
    @ResponseBody
    public String saveUser(@ModelAttribute User user){
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(user);
        if(ObjectUtils.isEmpty(user)) return "회원가입에 실패하였습니다.";

        String saveMsg = userSvc.saveUser(user);

        return saveMsg;
    }
    @GetMapping("/find")
    public String findPage(){
        return "login/find";
    }

    @PostMapping("/finduser")
    public String findUser(@ModelAttribute User user, Model md){
        if(ObjectUtils.isEmpty(user)){
            md.addAttribute("msg","다시 시도해주세요.");
            return "login/find";
        }
        User user2 = this.userSvc.updUser(user);
        if(ObjectUtils.isEmpty(user2)) {
            md.addAttribute("msg","ERROR");
            return "login/find";
        }
        return VIEW_NAME;
    }

}
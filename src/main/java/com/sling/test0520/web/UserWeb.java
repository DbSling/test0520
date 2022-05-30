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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import static com.sling.test0520.support.security.SecurityInterceptor.ID_COOKIE_NAME;

@Controller
@RequiredArgsConstructor
public class UserWeb {

    private static final String VIEW_LOGIN = "login/loginHome";
    private static final String VIEW_HOME = "home/home";

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userSvc;

    @GetMapping("/")
    public String userLogin(Model md, HttpServletRequest req) {
        md.addAttribute("login", new User());
        if(ObjectUtils.isEmpty(req.getCookies())) {
            md.addAttribute("notlogin", true);
            return VIEW_LOGIN;
        }
        //로그인 했을 경우
        return VIEW_HOME;
    }

    @PostMapping("/")
    public String loginSubmit(@ModelAttribute @Valid Login user, BindingResult errors, SessionStatus state, HttpServletRequest req, HttpServletResponse res)
            throws UnsupportedEncodingException {
        if (!StringUtils.hasText(user.getEmail())) {
            errors.reject("login", "입력 부탁드립니다.");
            return VIEW_LOGIN;
        }

        User userInfo = userSvc.userLogin(user);

        if (userInfo == null) {
            errors.reject("login", "이메일 또는 패스워드가 정확하지 않습니다");
            return VIEW_LOGIN;
        }
        state.setComplete();
        Cookie idCookie = new Cookie(ID_COOKIE_NAME, "" + userInfo.getId());
        Cookie nameCookie = new Cookie(SecurityInterceptor.NAME_COOKIE_NAME, URLEncoder.encode(userInfo.getName(), "UTF-8"));
        Cookie ipCookie = new Cookie(SecurityInterceptor.IP_COOKIE_NAME, req.getRemoteAddr());
        idCookie.setPath("/");
        nameCookie.setPath("/");
        ipCookie.setPath("/");
        res.addCookie(idCookie);
        res.addCookie(nameCookie);
        res.addCookie(ipCookie);

        logger.debug("Logged in : " + userInfo.toString() + " / " + req.getRemoteAddr());

        return VIEW_HOME;

    }

    //회원 가입 페이지 이동
    @GetMapping("/join")
    public String join() {
        return "login/join";
    }


    //회원가입 버튼 클릭
    @PostMapping("/saveUser")
    @ResponseBody
    public String saveUser(@RequestBody User user) {
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(user);
        if (ObjectUtils.isEmpty(user)) return "회원가입에 실패하였습니다.";

        return userSvc.saveUser(user);
    }

    //비밀번호 변경 페이지로 이동
    @GetMapping("/find")
    public String findPage(Model md) {
        md.addAttribute("login", new User());
        return "login/find";
    }

    //변경페이지에서 비밀번호 수정
    @PostMapping("/finduser")
    public String findUser(@ModelAttribute Login user, BindingResult err) {
        err.reject("login","비밀번호가 수정 되었습니다.");
        //이메일 입력안했을시
        /*if (!StringUtils.hasText(user.getEmail())) {
            err.reject("login", "이메일을 입력해주세요.");
            return "login/find";
        }*/
        User user2 = this.userSvc.updPassword(user);

        //일치하는 데이터가 없을 시
        if (ObjectUtils.isEmpty(user2)) {
            err.reject("login", "잘못된 데이터 입력.");
            return "login/find";
        }
        return VIEW_LOGIN;
    }

    //로그아웃
    @GetMapping("/logOut")
    public String logout(HttpServletRequest req,HttpServletResponse res) {
        for (Cookie c : req.getCookies()) {
            c.setMaxAge(0);
            res.addCookie(c);
        }
        return "redirect:";
    }

    //정보 수정
    @GetMapping("/myPage")
    public String myPage(Model md, HttpServletRequest req) {
        User user = this.userSvc.findMyPage(callCookie(req));
        //쿠키가 저장이 안되었거나 값이 잘 못 저장 되었을 때
        if (ObjectUtils.isEmpty(user)) {
            md.addAttribute("msg","정기점검");
            return VIEW_HOME;
        }
        md.addAttribute("User", user);
        return "home/myPage";
    }

    //myPage에서 저장버튼 클릭
    @PostMapping("/updUser")
    @ResponseBody
    public String updUser(@RequestBody User user){
        /*if(!StringUtils.hasText(user.getEmail())){
            return "데이터가 없다!!!";
        }*/
        return userSvc.saveUser(user);
    }

    //쿠키에 저장 되어있는 회원 고유아이디 호출
    public Long callCookie(HttpServletRequest req){

        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(ID_COOKIE_NAME)) {
                return Long.valueOf(c.getValue());
            }
        }
        return null;
    }

    //이메일찾기
    @GetMapping("/findMyEmail")
    @ResponseBody
    public String findMyEmail(@RequestBody User user){
        if(ObjectUtils.isEmpty(user)){
            return null;
        }
        return userSvc.findMyEmail(user.getName(),user.getPhoneNum());
    }
}
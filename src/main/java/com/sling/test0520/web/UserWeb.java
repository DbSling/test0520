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
import java.time.LocalDateTime;

import static com.sling.test0520.support.security.SecurityInterceptor.ID_COOKIE_NAME;

@Controller
//@SessionAttributes("login")
@RequiredArgsConstructor
public class UserWeb {

    private static final String VIEW_LOGIN = "login/loginHome";
    private static final String VIEW_HOME = "home/home";

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userSvc;

    @GetMapping("/")
    public String userLogin(@RequestParam(required = false) String userData, Model md, HttpServletRequest req) {
        md.addAttribute("login", new User());
        if(ObjectUtils.isEmpty(req.getCookies())) {
            if (userData != null) {
                md.addAttribute("notlogin", true);
            }
            return VIEW_LOGIN;
        }
        //로그인 했을 경우
        return VIEW_HOME;
    }

    @PostMapping("/")
    public String loginSubmit(@ModelAttribute @Valid Login user, BindingResult errors, SessionStatus state, HttpServletRequest req, HttpServletResponse res)
            throws UnsupportedEncodingException {
        if (ObjectUtils.isEmpty(user)) {
            errors.reject("login", "이메일 또는 패스워드가 정확하지 않습니다");
            return VIEW_LOGIN;
        }

        User userInfo = userSvc.userLogin(user);

        if (userInfo == null) {
            errors.reject("login", "이메일 또는 패스워드가 정확하지 않습니다");
            return VIEW_LOGIN;
        } else {
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
    }

    //이건 뭐지?
    //@GetMapping("/home")
    //public String home() {
    //    return "home";
    //}

    //회원 가입 페이지 이동
    @GetMapping("/join")
    public String join() {
        return "login/join";
    }


    //회원가입 버튼 클릭
    @PostMapping("/saveUser")
    @ResponseBody
    public String saveUser(@ModelAttribute User user) {
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(user);
        if (ObjectUtils.isEmpty(user)) return "회원가입에 실패하였습니다.";

        String saveMsg = userSvc.saveUser(user);

        return saveMsg;
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
        //데이터가 안넘어 올시
        if (ObjectUtils.isEmpty(user)) {
            err.reject("login", "데이터가 넘어가지 않음.");
            return "login/find";
        }
        User user2 = this.userSvc.updUser(user);

        //일치하는 데이터가 없을 시
        if (ObjectUtils.isEmpty(user2)) {
            err.reject("login", "잘못된 데이터 입력.");
            return "login/find";
        }
        return VIEW_LOGIN;
    }

    //로그아웃
    @GetMapping("/logOut")
    public String logout(HttpServletRequest req) {
        for (Cookie c : req.getCookies()) {
            c.setMaxAge(0);
        }
        return "redirect:";
    }

    //정보 수정
    @GetMapping("/myPage")
    public String myPage(HttpServletRequest req, Model md) {
        User user = null;
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(ID_COOKIE_NAME)) {
                user = this.userSvc.findById(c.getValue());
            }
        }
        if (ObjectUtils.isEmpty(user)) {
            md.addAttribute("msg","정기점검");
            return VIEW_HOME;
        }
        md.addAttribute("User", user);
        return "home/myPage";
    }
}
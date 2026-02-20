package org.chore.hotelsys.controller;

import org.chore.hotelsys.common.R;
import org.chore.hotelsys.dto.LoginDto;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Ensure cross-origin is allowed if needed, though proxy handles it usually
public class AuthController {

    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        // Mock authentication
        // In a real app, validate against database (e.g. Employee table)
        if ("admin".equals(loginDto.getUsername()) || "editor".equals(loginDto.getUsername())) {
             Map<String, String> data = new HashMap<>();
             data.put("token", loginDto.getUsername() + "-token");
             return R.success(data);
        }
        return R.error("用户名或密码错误");
    }

    @GetMapping("/info")
    public R<Map<String, Object>> info(@RequestParam("token") String token) {
        Map<String, Object> data = new HashMap<>();
        if ("admin-token".equals(token)) {
            data.put("roles", Arrays.asList("admin"));
            data.put("introduction", "I am a super administrator");
            data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            data.put("name", "Super Admin");
            return R.success(data);
        } else if ("editor-token".equals(token)) {
            data.put("roles", Arrays.asList("editor"));
            data.put("introduction", "I am an editor");
            data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            data.put("name", "Normal Editor");
            return R.success(data);
        }
        return R.error("Login failed, unable to get user details.");
    }

    @PostMapping("/logout")
    public R<String> logout() {
        return R.success("success");
    }
}

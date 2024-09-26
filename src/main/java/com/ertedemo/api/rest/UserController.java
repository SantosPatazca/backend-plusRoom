package com.ertedemo.api.rest;

import com.ertedemo.api.resource.users.CreateUserResource;
import com.ertedemo.api.resource.users.LoginCredential;
import com.ertedemo.api.resource.users.UpdateUserResource;
import com.ertedemo.api.resource.users.UserResponse;
import com.ertedemo.domain.model.entites.User;
import com.ertedemo.domain.services.UserService;
import com.ertedemo.shared.response.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse<UserResponse>> getAllUsers() {
        List<UserResponse> responseList = userService.getAll().stream()
                .map(user -> new UserResponse(user))
                .collect(Collectors.toList());

        BaseResponse<UserResponse> response = new BaseResponse<>(responseList, null, null, false);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{userId}")
    public ResponseEntity<BaseResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getById(userId);
        if (user.isPresent()) {
            BaseResponse<UserResponse> response = new BaseResponse<>(new UserResponse(user.get()), null, null, false);
            return ResponseEntity.ok(response);
        } else {
            BaseResponse<UserResponse> response = new BaseResponse<>(null, "404", "User not found", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Long>> createUser(@RequestBody CreateUserResource resource) {
        Optional<User> user = userService.create(new User(resource));

        if (user.isPresent()) {
            BaseResponse<Long> response = new BaseResponse<>(user.get().getId(), null, null, false);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            BaseResponse<Long> response = new BaseResponse<>(null, "500", "Internal Server Error", true);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping()
    public ResponseEntity<BaseResponse<UserResponse>> updateUser(@RequestBody UpdateUserResource resource) {
        Optional<User> user = userService.getById(resource.getId());

        if (user.isEmpty()) {
            BaseResponse<UserResponse> response = new BaseResponse<>(null, "400", "Invalid user ID", true);
            return ResponseEntity.badRequest().body(response);
        }

        user.get().updateUser(resource);
        User updatedUser = userService.update(user.get()).get();
        BaseResponse<UserResponse> response = new BaseResponse<>(new UserResponse(updatedUser), null, null, false);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long userId) {
        ResponseEntity<?> result = userService.delete(userId);
        if (result.getStatusCode() == HttpStatus.OK) {
            BaseResponse<Void> response = new BaseResponse<>(null, null, null, false);
            return ResponseEntity.ok(response);
        } else {
            BaseResponse<Void> response = new BaseResponse<>(null, "404", "User not found", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/rate-user/{userId}/{rate}")
    public ResponseEntity<BaseResponse<UserResponse>> rateUser(@PathVariable Long userId, @PathVariable Float rate) {
        Optional<User> user = userService.getById(userId);
        if (rate < 0 || rate > 5 || user.isEmpty()) {
            BaseResponse<UserResponse> response = new BaseResponse<>(null, "400", "Invalid rating or user ID", true);
            return ResponseEntity.badRequest().body(response);
        }

        user.get().setRankPoints(rate);
        Optional<User> updatedUser = userService.update(user.get());

        BaseResponse<UserResponse> response = new BaseResponse<>(new UserResponse(updatedUser.get()), null, null, false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Long>> login(@RequestBody LoginCredential loginCredential) {
        Long userId = userService.login(loginCredential.getEmail(), loginCredential.getPassword());
        if (userId != null) {
            BaseResponse<Long> response = new BaseResponse<>(userId, null, null, false);
            return ResponseEntity.ok(response);
        } else {
            BaseResponse<Long> response = new BaseResponse<>(null, "401", "Invalid login credentials", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

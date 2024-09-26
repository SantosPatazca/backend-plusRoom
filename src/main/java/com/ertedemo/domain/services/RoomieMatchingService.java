package com.ertedemo.domain.services;

import com.ertedemo.domain.model.entites.User;

import java.util.List;

public interface RoomieMatchingService {
    List<User> findMatches(User user);
}
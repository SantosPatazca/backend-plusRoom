package com.ertedemo.domain.services;

import com.ertedemo.domain.model.entites.RoomiePreference;
import com.ertedemo.domain.model.entites.User;

import java.util.List;

public interface RoomiePreferenceService {
    RoomiePreference savePreferences(RoomiePreference preferences);
    List<User> findByLocationAndBudget(String location, Float budget);
    RoomiePreference findByUserId(Long userId);
}
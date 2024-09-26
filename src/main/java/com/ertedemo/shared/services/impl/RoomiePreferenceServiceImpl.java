package com.ertedemo.shared.services.impl;

import com.ertedemo.domain.model.entites.RoomiePreference;
import com.ertedemo.domain.model.entites.User;
import com.ertedemo.domain.persistence.RoomiePreferenceRepository;
import com.ertedemo.domain.persistence.UserRepository;
import com.ertedemo.domain.services.RoomiePreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomiePreferenceServiceImpl implements RoomiePreferenceService {

    @Autowired
    private RoomiePreferenceRepository roomiePreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RoomiePreference savePreferences(RoomiePreference preferences) {
        return roomiePreferenceRepository.save(preferences);
    }

    @Override
    public List<User> findByLocationAndBudget(String location, Float budget) {
        List<RoomiePreference> preferences = roomiePreferenceRepository.findAll();
        return preferences.stream()
                .filter(pref -> (location == null || location.isEmpty() || pref.getLocationPreference().equals(location)) &&
                        (budget == null || pref.getBudget().equals(budget)))
                .map(pref -> userRepository.findById(pref.getUserId()).orElse(null))
                .collect(Collectors.toList());
    }
    @Override
    public RoomiePreference findByUserId(Long userId) {
        return roomiePreferenceRepository.findByUserId(userId);
    }
}
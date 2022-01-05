package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.Application;
import ru.team.up.sup.core.entity.User;

import java.util.List;

public interface ApplicationService {
    List<Application> getAllApplicationsByEventId(Long id);

    List<Application> getAllApplicationsByUserId(Long id);

    Application getApplication(Long id);

    Application saveApplication(Application application, User user);

    void deleteApplication(Long id);
}

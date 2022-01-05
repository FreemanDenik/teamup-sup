package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.Moderator;

import java.util.List;

/**
 * @author Alexey Tkachenko
 */
public interface ModeratorService {
    List<Moderator> getAllModerators();

    Moderator getOneModerator(Long id);

    Moderator saveModerator(Moderator user);

    void deleteModerator(Long id);
    boolean moderatorIsExistsById(Long id);

}

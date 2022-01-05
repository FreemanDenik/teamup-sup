package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team.up.sup.core.entity.Account;
import ru.team.up.sup.core.entity.Moderator;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {
    Account findByEmail(String email);
}

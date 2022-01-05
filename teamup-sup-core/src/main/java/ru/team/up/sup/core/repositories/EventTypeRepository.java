package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team.up.sup.core.entity.EventType;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
}

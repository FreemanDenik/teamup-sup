package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> getParametersBySystemName(String systemName);

    Parameter getParameterByParameterName(String parameterName);

    Parameter getParameterById(Long id);
}

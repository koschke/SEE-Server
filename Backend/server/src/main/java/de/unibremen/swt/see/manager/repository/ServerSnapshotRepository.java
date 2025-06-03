package de.unibremen.swt.see.manager.repository;

import de.unibremen.swt.see.manager.model.Server;
import de.unibremen.swt.see.manager.model.ServerSnapshot;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerSnapshotRepository extends JpaRepository<ServerSnapshot, UUID> {

    Optional<List<ServerSnapshot>> findAllByServer(@NotNull Server server);

}

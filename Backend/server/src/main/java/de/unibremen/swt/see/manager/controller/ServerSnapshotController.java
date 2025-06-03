package de.unibremen.swt.see.manager.controller;

import de.unibremen.swt.see.manager.model.ServerSnapshot;
import de.unibremen.swt.see.manager.service.ServerSnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/serversnapshot")
@RequiredArgsConstructor
@Slf4j
public class ServerSnapshotController {

    /**
     * Used to access server snapshots.
     */
    private final ServerSnapshotService serverSnapshotService;


}

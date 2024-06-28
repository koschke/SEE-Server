package de.unibremen.swt.see.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents the data model of files that are exchanged via REST API.
 * <p>
 * Possible types are defined in related {@code enum} {@link FileType}.
 */
@Getter
@Entity
@Table(name = "files")
@RequiredArgsConstructor
public class File {

    /**
     * ID of the file.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    /**
     * The original name of the uploaded file.
     */
    @Setter
    private String originalFileName;

    /**
     * The intended name of the uploaded file.
     * <p>
     * This is usually set during upload to match the use case.
     */
    @Setter
    private String name;

    /**
     * The original content type of the uploaded file.
     * <p>
     * The content type is usually represented by a MIME type
     * and set by the client during upload.
     */
    @Setter
    private String contentType;

    /**
     * The intended purpose of the file.
     * <p>
     * The intended purpose is set by the client during upload.
     * It represents the function in a SEE Code City configuration.
     */
    @Setter
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    /**
     * The server this file is associated with.
     */
    @JsonIgnore
    @ManyToOne
    @Setter
    @JoinColumn(name = "server_id")
    private Server server;

    /**
     * The point in time this file was created.
     * <p>
     * This data is set by the server during upload.
     * Thus it is dependent on the host's time configuration.
     * However, the timestamp is generated as UTC.
     */
    @Column(name = "creation_time", updatable = false)
    private ZonedDateTime creationTime;

    /**
     * Generates the timestamp for the {@link creationTime}.
     * <p>
     * The timestamp is generated right before this metadata object is
     * persisted in the database.
     * It is generated using UTC timezone.
     */
    @PrePersist
    void generateTimeStamp() {
        creationTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

}

package de.unibremen.swt.see.manager.util;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.RoomServiceClient;
import java.io.IOException;
import java.util.UUID;
import livekit.LivekitModels;
import retrofit2.Call;
import retrofit2.Response;

/**
 * This singleton manages the LiveKit rooms and tokens.
 */
public class LiveKitManager {

    public static long DEFAULT_TTL = 21600000; // ms = 6 h

    /**
     * Singleton instance.
     */
    private static final LiveKitManager instance = new LiveKitManager();

    private final RoomServiceClient client;

    /**
     * Private constructor to prevent instantiation (singleton).
     */
    private LiveKitManager() {
        // TODO Collect URL and secrets from configuration environment
        // LIVEKIT_API_KEY
        // LIVEKIT_API_SECRET
        client = RoomServiceClient.createClient(
                "http://example.com",
                "apiKey",
                "secret");
    }

    /**
     * @return the {@code LiveKitTokenManager} instance
     */
    public static LiveKitManager getInstance() {
        return instance;
    }

    /**
     * Delete an existing video conferencing room.
     *
     * @param serverId SEE server id that the video conference belongs to
     * @throws IOException if an I/O error is encountered while accessing the
     * server
     */
    protected void deleteRoom(UUID serverId) throws IOException {
        Call<Void> call = client.deleteRoom(serverId.toString());
        Response<Void> response = call.execute();
        if (!response.isSuccessful()) {
            // TODO throw custom exception
        }
    }

    /**
     * Returns a room associated to the given {@code serverId}.
     * <br>
     * The room will either be reused if it already exists or created if not.
     *
     * @param serverId SEE server id that the video conference belongs to
     * @throws IOException if an I/O error is encountered while accessing the
     * server
     */
    private String getOrCreateRoom(UUID serverId) throws IOException {
        Call<LivekitModels.Room> call = client.createRoom(serverId.toString());
        Response<LivekitModels.Room> response = call.execute();
        if (response.isSuccessful()) {
            LivekitModels.Room room = response.body();
            if (room != null) {
                return room.getName();
            }
        } else {
            // TODO does room already exist and can be reused?
        }
        return null;
    }

    /**
     * Generates an access token that can be used to access a video conferencing
     * room.
     * <br>
     * The room name is generated based on the given {@code serverId}.
     *
     * @param serverId SEE server id that the video conference belongs to
     * @param participantName name of the participant
     * @param ttl token lifetime in ms (time to live)
     * @return JWT access token
     * @throws IOException if an I/O error is encountered while accessing the
     * server
     */
    protected String getToken(UUID serverId, String participantName, long ttl) throws IOException {
        String roomName = getOrCreateRoom(serverId);
        if (roomName == null || roomName.isBlank()) {
            return null;
        }

        AccessToken token = new AccessToken("apiKey", "secret");

        // TODO Fill in token information.
        token.setName(participantName);
        token.setIdentity("identity"); // Unique identity of the user, required for room join tokens
        // token.setMetadata("metadata"); // Custom metadata to be passed to participants
        token.addGrants(new RoomJoin(true), new RoomName(roomName));
        token.setTtl(ttl);

        return token.toJwt();
    }

    /**
     * Generates an access token that can be used to access a video conferencing
     * room.
     * <br>
     * This is a convenience function that sets the token lifetime to the
     * default value.
     *
     * @param serverId SEE server id that the video conference belongs to
     * @param participantName name of the participant
     * @return JWT access token
     * @throws IOException if an I/O error is encountered while accessing the
     * server
     * @see getToken(UUID, String, long)
     */
    protected String getToken(UUID serverId, String participantName) throws IOException {
        return getToken(serverId, participantName, DEFAULT_TTL);
    }

}

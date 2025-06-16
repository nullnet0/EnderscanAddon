package xyz.nullnetdev.enderscanaddon.utils.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FoundServer(
    String ip,
    String last_scanned,
    int online,
    int max_players,
    String motd,
    String version,

    int id,
    String status
) {
}

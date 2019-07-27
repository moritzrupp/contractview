package de.moritzrupp.contractview.service.dto;

import de.moritzrupp.contractview.domain.Contract;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * A DTO representing Contracts as events.
 */
public class EventDTO {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String provider;

    @NotBlank
    private Instant contractStart;

    @NotBlank
    private Instant contractEnd;

    public EventDTO() {
        // Empty constructor needed for Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Instant getContractStart() {
        return contractStart;
    }

    public void setContractStart(Instant contractStart) {
        this.contractStart = contractStart;
    }

    public Instant getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(Instant contractEnd) {
        this.contractEnd = contractEnd;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", provider='" + provider + '\'' +
            ", contractStart=" + contractStart +
            ", contractEnd=" + contractEnd +
            '}';
    }
}

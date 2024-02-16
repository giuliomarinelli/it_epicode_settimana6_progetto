package it.epicode.w6d5.devices_management.Models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.w6d5.devices_management.Models.enums.DeviceType;
import it.epicode.w6d5.devices_management.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Entity
@Data
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean assigned = false;

    @Column(nullable = false)
    private boolean underMaintenance;

    @Column(nullable = false)
    private boolean neglected;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @Transient
    private UUID employeeId;

    public Device(boolean available, boolean underMaintenance, boolean neglected, String type) throws BadRequestException {
        this.available = available;
        this.underMaintenance = underMaintenance;
        this.neglected = neglected;
        try {
        this.type = DeviceType.valueOf(type); // un controllo aggiuntivo a quello fatto attraverso la validazione
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Malformed 'type' field, allowed exact-match values are SMARTPHONE, TABLET, LAPTOP, DOMOTIC_DEVICE," +
                    " DIGITAL_CAMERA, SMART_CARD, DESKTOP_COMPUTER, TV, OTHERS");
        }
    }
}

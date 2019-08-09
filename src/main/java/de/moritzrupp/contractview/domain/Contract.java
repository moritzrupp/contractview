package de.moritzrupp.contractview.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_email")
    private String contactEmail;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Min(value = 0)
    @Column(name = "billing_period_days", nullable = false)
    private Integer billingPeriodDays;

    @NotNull
    @Column(name = "contract_start", nullable = false)
    private Instant contractStart;

    @Column(name = "contract_end")
    private Instant contractEnd;

    @NotNull
    @Column(name = "automatic_extension", nullable = false)
    private Boolean automaticExtension;

    @NotNull
    @Min(value = 0)
    @Column(name = "extension_period_days", nullable = false)
    private Integer extensionPeriodDays;

    @NotNull
    @Column(name = "extension_reminder", nullable = false)
    private Boolean extensionReminder;

    @NotNull
    @Min(value = 0)
    @Column(name = "extension_reminder_period_days", nullable = false)
    private Integer extensionReminderPeriodDays;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "contract_users",
               joinColumns = @JoinColumn(name = "contracts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Contract name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Contract contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Double getPrice() {
        return price;
    }

    public Contract price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getBillingPeriodDays() {
        return billingPeriodDays;
    }

    public Contract billingPeriodDays(Integer billingPeriodDays) {
        this.billingPeriodDays = billingPeriodDays;
        return this;
    }

    public void setBillingPeriodDays(Integer billingPeriodDays) {
        this.billingPeriodDays = billingPeriodDays;
    }

    public Instant getContractStart() {
        return contractStart;
    }

    public Contract contractStart(Instant contractStart) {
        this.contractStart = contractStart;
        return this;
    }

    public void setContractStart(Instant contractStart) {
        this.contractStart = contractStart;
    }

    public Instant getContractEnd() {
        return contractEnd;
    }

    public Contract contractEnd(Instant contractEnd) {
        this.contractEnd = contractEnd;
        return this;
    }

    public void setContractEnd(Instant contractEnd) {
        this.contractEnd = contractEnd;
    }

    public Boolean isAutomaticExtension() {
        return automaticExtension;
    }

    public Contract automaticExtension(Boolean automaticExtension) {
        this.automaticExtension = automaticExtension;
        return this;
    }

    public void setAutomaticExtension(Boolean automaticExtension) {
        this.automaticExtension = automaticExtension;
    }

    public Integer getExtensionPeriodDays() {
        return extensionPeriodDays;
    }

    public Contract extensionPeriodDays(Integer extensionPeriodDays) {
        this.extensionPeriodDays = extensionPeriodDays;
        return this;
    }

    public void setExtensionPeriodDays(Integer extensionPeriodDays) {
        this.extensionPeriodDays = extensionPeriodDays;
    }

    public Boolean isExtensionReminder() {
        return extensionReminder;
    }

    public Contract extensionReminder(Boolean extensionReminder) {
        this.extensionReminder = extensionReminder;
        return this;
    }

    public void setExtensionReminder(Boolean extensionReminder) {
        this.extensionReminder = extensionReminder;
    }

    public Integer getExtensionReminderPeriodDays() {
        return extensionReminderPeriodDays;
    }

    public Contract extensionReminderPeriodDays(Integer extensionReminderPeriodDays) {
        this.extensionReminderPeriodDays = extensionReminderPeriodDays;
        return this;
    }

    public void setExtensionReminderPeriodDays(Integer extensionReminderPeriodDays) {
        this.extensionReminderPeriodDays = extensionReminderPeriodDays;
    }

    public Provider getProvider() {
        return provider;
    }

    public Contract provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public User getOwner() {
        return owner;
    }

    public Contract owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Contract users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Contract addUsers(User user) {
        this.users.add(user);
        user.getContracts().add(this);
        return this;
    }

    public Contract removeUsers(User user) {
        this.users.remove(user);
        user.getContracts().remove(this);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if (contract.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contract.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", price=" + getPrice() +
            ", billingPeriodDays=" + getBillingPeriodDays() +
            ", contractStart='" + getContractStart() + "'" +
            ", contractEnd='" + getContractEnd() + "'" +
            ", automaticExtension='" + isAutomaticExtension() + "'" +
            ", extensionPeriodDays=" + getExtensionPeriodDays() +
            ", extensionReminder='" + isExtensionReminder() + "'" +
            ", extensionReminderPeriodDays=" + getExtensionReminderPeriodDays() +
            "}";
    }
}

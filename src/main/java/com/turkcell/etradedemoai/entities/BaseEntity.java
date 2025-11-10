package com.turkcell.etradedemoai.entities;

/**
 * Entities package-level BaseEntity that extends the shared common BaseEntity.
 * This class intentionally extends `com.turkcell.etradedemoai.common.BaseEntity`
 * so JPA entities in this package can inherit common fields and behavior.
 */
public abstract class BaseEntity extends com.turkcell.etradedemoai.common.BaseEntity {
    // marker subclass - common fields live in common.BaseEntity
}

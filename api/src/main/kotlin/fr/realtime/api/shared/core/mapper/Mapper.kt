package fr.realtime.api.shared.core.mapper

interface Mapper<D, E> {
    fun entityToDomain(entity: E): D
    fun domainToEntity(domain: D): E
}
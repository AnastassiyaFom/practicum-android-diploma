package ru.practicum.android.diploma.favorites.data.db

import ru.practicum.android.diploma.search.domain.models.Vacancy

object VacancyDbConverter {
    fun map(vacancy: Vacancy): VacancyEntity {
        val phonesString = vacancy.phones?.joinToString(separator = ",")
        val skillsString = vacancy.skills?.joinToString(separator = ",")
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            vacancyTitle = vacancy.vacancyTitle,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            areaName = vacancy.areaName,
            industryName = vacancy.industryName,
            skills = skillsString,
            url = vacancy.url,
            // Поля зарплаты
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            currency = vacancy.currency,
            salaryTitle = vacancy.salaryTitle,
            // Поля адреса
            city = vacancy.city,
            street = vacancy.street,
            building = vacancy.building,
            fullAddress = vacancy.fullAddress,
            // Контакты
            contactName = vacancy.contactName,
            email = vacancy.email,
            phones = phonesString,
            // Работодатель
            employerName = vacancy.employerName,
            logoUrl = vacancy.logoUrl
        )
    }

    fun map(vacancy: VacancyEntity?): Vacancy {
        if (vacancy == null) return Vacancy()
        val phones: List<String> = vacancy.phones
            ?.split(",")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
        val skills: List<String> = vacancy.skills?.split(",") ?: emptyList()
        return Vacancy(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description ?: "",
            vacancyTitle = vacancy.vacancyTitle,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            areaName = vacancy.areaName,
            industryName = vacancy.industryName,
            skills = skills,
            url = vacancy.url,
            // Поля зарплаты
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            currency = vacancy.currency,
            salaryTitle = vacancy.salaryTitle,
            // Поля адреса
            city = vacancy.city,
            street = vacancy.street,
            building = vacancy.building,
            fullAddress = vacancy.fullAddress,
            // Контакты
            contactName = vacancy.contactName,
            email = vacancy.email,
            phones = phones,
            // Работодатель
            employerName = vacancy.employerName,
            logoUrl = vacancy.logoUrl
        )
    }
}

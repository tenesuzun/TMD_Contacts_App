package com.example.tmdcontactsapp.models

import com.example.tmdcontactsapp.R

class DataSource {

    companion object {

        fun create_contacts_list(): List<Contact> {
            return listOf(
                Contact(R.drawable.ic_baseline_person_24, "Taha Enes", "Uzun"),
                Contact(R.drawable.ic_baseline_person_24, "Berkay", "Özgen"),
                Contact(R.drawable.ic_baseline_person_24, "Bedirhan", "Savaş"),
                Contact(R.drawable.ic_baseline_person_24, "Hüseyin Faruk", "Çökmez"),
                Contact(R.drawable.ic_baseline_person_24, "Fırat", "Albayatı")
            )
        }
    }
}
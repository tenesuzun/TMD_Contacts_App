package com.example.tmdcontactsapp.datasource

import com.example.tmdcontactsapp.models.Contact

fun contactsList(): List<Contact>{
    return listOf(
        Contact("","Taha Enes","Uzun"),
        Contact("","Berkay","Özgen"),
        Contact("","Bedirhan","Savaş"),
        Contact("","Hüseyin Faruk","Çökmez"),
        Contact("","Fırat","Albayatı")
    )
}

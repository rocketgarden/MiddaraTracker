package net.vinceblas.middaratracker

import androidx.annotation.DrawableRes

data class Combatant(
    val name: String,
    @DrawableRes val image: Int
)


val regularCards = listOf(
    // PCs
    Combatant("Nightingale", R.drawable.card_nightingale),
    Combatant("Remi", R.drawable.card_remi),
    Combatant("Rook", R.drawable.card_rook),
    Combatant("Zeke", R.drawable.card_zeke),
    // Friendly summons
    Combatant("Twindiem", R.drawable.card_twindiem),
    Combatant("Twinfemke", R.drawable.card_twinfemke),
    Combatant("Zulfiqar", R.drawable.card_zulfiqar),
    // enemies
    Combatant("Animate", R.drawable.card_animate),
    Combatant("Blightedguardian", R.drawable.card_blightedguardian),
    Combatant("Cavesickle_a", R.drawable.card_cavesickle_a),
    Combatant("Cavesickle_b", R.drawable.card_cavesickle_b),
    Combatant("Earthloa", R.drawable.card_earthloa),
    Combatant("Fragor", R.drawable.card_fragor),
    Combatant("Gevaudan", R.drawable.card_gevaudan),
    Combatant("Livingoblation", R.drawable.card_livingoblation),
    Combatant("Merkhound", R.drawable.card_merkhound),
    Combatant("Waterloa", R.drawable.card_waterloa),
    Combatant("Gatekeeper", R.drawable.card_gatekeeper),
    Combatant("Torturedimmortal", R.drawable.card_torturedimmortal),
    // enemy summons
    Combatant("Celestialgigas", R.drawable.card_celestialgigas),
    Combatant("Spiritblade", R.drawable.card_spiritblade),
    Combatant("Twinava", R.drawable.card_twinava),
    Combatant("Twinhope", R.drawable.card_twinhope),
    Combatant("Visceraarimid", R.drawable.card_visceraarimid),
    // other summons
    Combatant("Agares", R.drawable.card_agares),
    Combatant("Eliphie", R.drawable.card_eliphie),
    Combatant("Enslavedspirit", R.drawable.card_enslavedspirit),
    // boss/special
    Combatant("Enslaved_a", R.drawable.card_enslaved_a),
    Combatant("Enslaved_b", R.drawable.card_enslaved_b),
    Combatant("Grotesqueeffigy", R.drawable.card_grotesqueeffigy),
    Combatant("Lichwurm", R.drawable.card_lichwurm),
    Combatant("Soulbutcher", R.drawable.card_soulbutcher),
    Combatant("Corpsecollector", R.drawable.card_corpsecollector),
)

val hiddenCards = listOf(
    Combatant("Aisling", R.drawable.hcard_aisling),
    Combatant("Alana", R.drawable.hcard_alana),
    Combatant("Alana_b", R.drawable.hcard_alana_b),
    Combatant("Ancientpixies", R.drawable.hcard_ancientpixies),
    Combatant("Brea", R.drawable.hcard_brea),
    Combatant("Breeze", R.drawable.hcard_breeze),
    Combatant("Casithus", R.drawable.hcard_casithus),
    Combatant("Daedalus", R.drawable.hcard_daedalus),
    Combatant("Damocles_f", R.drawable.hcard_damocles_f),
    Combatant("Damocles_r", R.drawable.hcard_damocles_r),
    Combatant("Echo_a", R.drawable.hcard_echo_a),
    Combatant("Echo_b", R.drawable.hcard_echo_b),
    Combatant("Echo_f", R.drawable.hcard_echo_f),
    Combatant("Ezra_a", R.drawable.hcard_ezra_a),
    Combatant("Ezra_b", R.drawable.hcard_ezra_b),
    Combatant("Hendrix_a", R.drawable.hcard_hendrix_a),
    Combatant("Hendrix_b", R.drawable.hcard_hendrix_b),
    Combatant("Isla", R.drawable.hcard_isla),
    Combatant("Justice", R.drawable.hcard_justice),
    Combatant("Padric", R.drawable.hcard_padric),
    Combatant("Quinn", R.drawable.hcard_quinn),
    Combatant("Rescindo_a", R.drawable.hcard_rescindo_a),
    Combatant("Rescindo_b", R.drawable.hcard_rescindo_b),
    Combatant("Shadowlord_a", R.drawable.hcard_shadowlord_a),
    Combatant("Shadowlord_b", R.drawable.hcard_shadowlord_b),
    Combatant("Shayliss", R.drawable.hcard_shayliss),
    Combatant("Sivguard_a", R.drawable.hcard_sivguard_a),
    Combatant("Sivguard_b", R.drawable.hcard_sivguard_b),
    Combatant("Soulsticher", R.drawable.hcard_soulsticher),
    Combatant("Suboblation", R.drawable.hcard_suboblation),
    Combatant("Suri", R.drawable.hcard_suri),
    Combatant("Titus", R.drawable.hcard_titus),
    Combatant("Ursy", R.drawable.hcard_ursy),
    Combatant("Zafir", R.drawable.hcard_zafir),
)
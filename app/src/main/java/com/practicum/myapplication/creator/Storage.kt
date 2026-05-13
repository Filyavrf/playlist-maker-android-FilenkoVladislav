package com.practicum.myapplication.creator

import com.practicum.myapplication.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            trackId = 1,
            trackName = "Владивосток 2000",
            artistName = "Мумий Троль",
            trackTimeMillis = 158000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 2,
            trackName = "Группа крови",
            artistName = "Кино",
            trackTimeMillis = 283000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 3,
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTimeMillis = 312000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 4,
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTimeMillis = 225000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 5,
            trackName = "Лондон",
            artistName = "Аквариум",
            trackTimeMillis = 272000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 6,
            trackName = "На заре",
            artistName = "Альянс",
            trackTimeMillis = 230000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 7,
            trackName = "Перемен",
            artistName = "Кино",
            trackTimeMillis = 296000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 8,
            trackName = "Розовый фламинго",
            artistName = "Сплин",
            trackTimeMillis = 195000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 9,
            trackName = "Танцевать",
            artistName = "Мельница",
            trackTimeMillis = 222000,
            artworkUrl100 = ""
        ),
        TrackDto(
            trackId = 10,
            trackName = "Чёрный бумер",
            artistName = "Серега",
            trackTimeMillis = 241000,
            artworkUrl100 = ""
        )
    )

    fun search(request: String): List<TrackDto> {
        val result = listTracks.filter { track ->
            track.trackName.contains(request, ignoreCase = true) ||
                    track.artistName.contains(request, ignoreCase = true)
        }
        return result
    }
}
package com.example.nasamarsrovers.utils

import com.example.nasamarsrovers.model.CameraHardware

const val API_KEY = "F1jrnZRZ2bdZmfx19dfiSvZypHVYk2rgMohbwVBG"
const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/"
const val CURIOSITY = "Curiosity"
const val SPIRIT = "Spirit"
const val OPPORTUNITY = "Opportunity"
val CURIOSITY_CAMERAS = mapOf<String, CameraHardware>(
    Pair("FHAZ", CameraHardware("Front Hazard Avoidance Camera", "FHAZ")),
    Pair("NAVCAM", CameraHardware("Navigation Camera", "NAVCAM")),
    Pair("MAST", CameraHardware("Mast Camera", "MAST")),
    Pair("CHEMCAM", CameraHardware("Chemistry and Camera Complex", "CHEMCAM")),
    Pair("MAHLI", CameraHardware("Mars Hand Lens Imager", "MAHLI")),
    Pair("MARDI", CameraHardware("Mars Descent Imager", "MARDI")),
    Pair("RHAZ", CameraHardware("Rear Hazard Avoidance Camera", "RHAZ"))
)
val SPIRIT_AND_OPPORTUNITY_CAMERAS = mapOf<String, CameraHardware>(
    Pair("FHAZ", CameraHardware("Front Hazard Avoidance Camera", "FHAZ")),
    Pair("RHAZ", CameraHardware("Rear Hazard Avoidance Camera", "RHAZ")),
    Pair("NAVCAM", CameraHardware("Navigation Camera", "NAVCAM")),
    Pair("PANCAM", CameraHardware("Panoramic Camera", "PANCAM")),
    Pair("MINITES", CameraHardware("Miniature Thermal Emission Spectrometer (Mini-TES)", "MINITES"))
)
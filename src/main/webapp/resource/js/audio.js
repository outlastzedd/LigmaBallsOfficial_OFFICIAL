// src/main/webapp/resource/js/audio.js
document.addEventListener("DOMContentLoaded", function () {
    let feinSound = document.getElementById("feinSound");
    let notLikeUsSound = document.getElementById("notLikeUsSound");
    let schyeahSound = document.getElementById("schyeahSound");

    // Setup audio elements
    if (feinSound) {
        feinSound.volume = 0.3; // Set volume to 30%
    } else {
        console.error("feinSound element not found!");
    }
    if (notLikeUsSound) {
        notLikeUsSound.volume = 0.3; // Set volume to 30%
    } else {
        console.error("notLikeUsSound element not found!");
    }
    if (schyeahSound) {
        schyeahSound.volume = 0.3; // Set volume to 30%
    } else {
        console.error("schyeahSound element not found!");
    }

    // Stop all audio when clicking outside
    document.addEventListener("click", function () {
        if (feinSound && !feinSound.paused) {
            feinSound.pause();
            feinSound.currentTime = 0;
        }
        if (notLikeUsSound && !notLikeUsSound.paused) {
            notLikeUsSound.pause();
            notLikeUsSound.currentTime = 0;
        }
        if (schyeahSound && !schyeahSound.paused) {
            schyeahSound.pause();
            schyeahSound.currentTime = 0;
        }
    });

    // Play audio function
    function playSound(audioElement) {
        if (audioElement) {
            // Stop all other audio tracks
            [feinSound, notLikeUsSound, schyeahSound].forEach(track => {
                if (track && track !== audioElement && !track.paused) {
                    track.pause();
                    track.currentTime = 0;
                }
            });

            audioElement.currentTime = 0;
            audioElement.play()
                .catch(error => console.error("Audio playback failed:", error));
        }
    }

    // Expose playAudio globally with track selection
    window.playAudio = function (track) {
        if (track === "fein" && feinSound) {
            playSound(feinSound);
        } else if (track === "notLikeUs" && notLikeUsSound) {
            playSound(notLikeUsSound);
        } else if (track === "schyeah" && schyeahSound) {
            playSound(schyeahSound);
        } else {
            console.error("Invalid track or audio element missing:", track);
        }
    };
});
// src/main/webapp/resource/js/chatbox.js
document.addEventListener("DOMContentLoaded", function () {
    let chatbox = document.getElementById("chatbox");
    let chatToggle = document.getElementById("chat-toggle");
    let inputField = document.getElementById("input");
    let messages = document.getElementById("messages");

    // Toggle chatbox
    chatToggle.addEventListener("click", function (event) {
        event.stopPropagation();
        chatbox.classList.toggle("minimized");
    });

    // Prevent clicks inside chatbox from closing it
    chatbox.addEventListener("click", function (event) {
        event.stopPropagation();
    });

    // Minimize chatbox when clicking outside
    document.addEventListener("click", function () {
        chatbox.classList.add("minimized");
    });

    // Send message function
    window.sendMessage = function () {
        let input = inputField.value.trim();
        if (input === "") return;

        // Check input against servlet conditions
        let lowerInput = input.toLowerCase();
        if (typeof window.playAudio === "function") {
            if (lowerInput.includes("travis scott") || lowerInput.includes("fe!n") || 
                lowerInput.includes("da den") || lowerInput.includes("người yêu")) {
                window.playAudio("fein"); // Play FE!N
            } else if (lowerInput.includes("kendrick lamar") || lowerInput.includes("say drake") || lowerInput.includes("a minor") || lowerInput.includes("ovho")) {
                window.playAudio("notLikeUs"); // Play Not Like Us
            } else if (lowerInput.includes("schyeah") || lowerInput.includes("carti")) {
                window.playAudio("schyeah"); // Play Schyeah
            }
        } else {
            console.error("playAudio function not defined!");
        }

        // Display user message
        let userMessage = document.createElement("div");
        userMessage.className = "message user-message";
        userMessage.textContent = input;
        messages.appendChild(userMessage);

        // Send message to server
        fetch("/LigmaBallsOfficial/ChatServlet", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8" },
            body: "message=" + encodeURIComponent(input)
        })
        .then(response => response.text())
        .then(data => {
            let botMessage = document.createElement("div");
            botMessage.className = "message bot-message";
            botMessage.textContent = data || "Chatbot chưa có phản hồi!";
            messages.appendChild(botMessage);
            messages.scrollTop = messages.scrollHeight;
        })
        .catch(error => {
            let errorMessage = document.createElement("div");
            errorMessage.className = "message bot-message";
            errorMessage.textContent = "Lỗi: " + error.message;
            messages.appendChild(errorMessage);
        });

        inputField.value = "";
    };
});
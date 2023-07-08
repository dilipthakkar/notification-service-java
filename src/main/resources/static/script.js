/**
 * Toast types corrosponding to the events.
 */
const ToastType = {
  SUCCESS: 'toast-success',
  ERROR: "toast-error",
  MESSAGE: 'toast-error',
  INFO: 'toast-info'
}

let eventSource;

/**
 * Connects client to the event source.
 * @returns 
 */
function connect() {
  userName = document.getElementById("userName").value;
  userNameErrorElement = document.getElementById("userNameError");

  if (!userName || userName.trim() == '') {
    userNameErrorElement.style.display = "block";
    return;
  } else {
    userNameErrorElement.style.display = "none";
  }

  eventSource = new EventSource(`http://localhost:8080/connect?userName=${userName}`);

  eventSource.onopen = function (event) {
    beforeConnectGroup = document.getElementById("before-connect-group")
    afterConnectGroup = document.getElementById("after-connect-group")

    beforeConnectGroup.style.display = "none"
    afterConnectGroup.style.display = "block"

    userNameReadOnlyInput = document.getElementById("userNameReadOnly");
    userNameReadOnlyInput.value = userName;
  }

  // Event listener for handling SSE errors
  eventSource.onerror = function (error) {
    console.error("Error occurred:", error);
  };

  // Event listener for handling SSE connection closure
  eventSource.onclose = function () {
    console.log("SSE connection closed");
  };

  // Event listener for handling successful connection
  eventSource.addEventListener("CONNECTED", (event) => {
    console.log("Connected");
    const { allUsers } = JSON.parse(event.data);
    updateUsersList(allUsers);
    createToast(ToastType.SUCCESS, "Connected")
  })

  // Event listener for handling a new user connection
  eventSource.addEventListener("ADD_USER", (event) => {
    console.log("A new User Added " + event.data);
    const { newAddedUserId, allUsers } = JSON.parse(event.data)
    createToast(ToastType.INFO, `${newAddedUserId} connected`)
    updateUsersList(allUsers);
  })
}

/**
 * Disconnects user from event source. 
 */
function disconnect() {
  eventSource.close();
  beforeConnectGroup = document.getElementById("before-connect-group")
  afterConnectGroup = document.getElementById("after-connect-group")

  beforeConnectGroup.style.display = "block"
  afterConnectGroup.style.display = "none"
}

/**
 * Displays a toast message
 * @param {String} type The type of toast to be displayed.
 * @param {String} toastMessage The text of toastMessage.
 */
function createToast(type, toastMessage) {
  const toastHtml = `<div class="custom-toast ${type}">
    <span class="toast-message">${toastMessage}</span>
  </div>`
  toastStack = document.getElementById("toast-stack");

  const toastContainer = document.createElement("div");
  toastContainer.innerHTML = toastHtml;
  toastStack.appendChild(toastContainer);

  // Delete toast element after 10 seconds
  setTimeout(function () {
    toastStack.removeChild(toastContainer);
  }, 10000);
}

/**
 * Updates all connected users list on UI.
 * @param {List<String>} allUsers 
 */
function updateUsersList(allUsers) {

  let allUsersListHtml = ""
  allUsers.forEach(userName => {
    allUsersListHtml += `<li class="list-group-item">${userName}</li>`
  });
  allUsersListHtml = `<ul class="list-group">${allUsersListHtml}<ul>`

  const allUsersList = document.getElementById("all-users-list");
  allUsersList.innerHTML = allUsersListHtml;

  listHeading = document.getElementById('all-users-list-heading')

  listHeading.textContent = allUsers.length > 0 ? "All Users" : "No Connected Users";
}


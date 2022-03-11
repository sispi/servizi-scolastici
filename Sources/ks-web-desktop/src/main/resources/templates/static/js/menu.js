const topnav = document.getElementById("k-topnav");
const topnavToggle = topnav.querySelector(".k-menu-toggle");

function openMobileNavbar() {
    topnav.classList.add("opened");
    topnavToggle.setAttribute("aria-label", "Close navigation menu.");
}

function closeMobileNavbar() {
    topnav.classList.remove("opened");
    topnavToggle.setAttribute("aria-label", "Open navigation menu.");
}

topnavToggle.addEventListener("click", () => {
    if (topnav.classList.contains("opened")) {
        closeMobileNavbar();
    } else {
        openMobileNavbar();
    }
});

const topnavMenu = topnav.querySelector(".k-nav-menu");
const topnavLinks = topnav.querySelector(".k-nav-links");

topnavLinks.addEventListener("click", (clickEvent) => {
    clickEvent.stopPropagation();
});

topnavMenu.addEventListener("click", closeMobileNavbar);

function setSearchBox(){
    var params = new URLSearchParams(location.search);
    if (params.get("qt") == "select")
        $("#k-searchbox input[name='text']").val(params.get("text"))
    else
        $("#k-searchbox input[name='text']").val('')
}

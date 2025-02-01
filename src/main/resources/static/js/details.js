$(document).ready(function () {
    // Retrieve stored document number from sessionStorage
    const foundDocumentNo = sessionStorage.getItem("foundDocumentNo");
    const foundDocumentType = sessionStorage.getItem("foundDocumentType");
    const custodianId = sessionStorage.getItem("custodianId")


    if (!foundDocumentNo && !foundDocumentType){
        alert("No document found. Please search a document first.");
        window.location.href = "/index";
        return;
    }
    

    // Fetch custodian details from the API
    $.ajax({
        url: `/api/v1/custodian/details/${encodeURIComponent(custodianId)}`,
        type: "GET",
        success: function (response) {
            // Dynamically update the document and custodian details
            console.log(response.fullName.split('')[0]);
            $("#documentType").text(foundDocumentType);
            $("#documentNo").text(foundDocumentNo);
            $("#custodianName").text(response.fullName);
            $("#custodianFirstName").text(response.fullName.split(' ')[0]);
            $("#custodianPhone").text("+"+response.phoneNumber);
        },
        error: function (error) {
            console.error("Error fetching custodian details:", error);
            alert("Failed to retrieve custodian details. Please try again.");
        }
    });
});

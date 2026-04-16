<?php
header('Content-Type: application/json'); // JSON output only, no HTML
require_once("databaseconnect.php"); // Make sure this file has no echo or whitespace

$response = array();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    // Get POST data safely
    $shopName      = $_POST['shopName'] ?? '';
    $shopAddress   = $_POST['shopAddress'] ?? '';
    $shopEmail     = $_POST['shopEmail'] ?? '';
    $finalName     = $_POST['finalUserName'] ?? '';
    $finalEmail    = $_POST['finalEmail'] ?? '';
    $finalDate     = $_POST['finalDate'] ?? '';
    $finalTime     = $_POST['finalTime'] ?? '';
    $finalService  = $_POST['services'] ?? '';
    $totalAmount   = $_POST['totalAmount'] ?? '';
    $paymentMode   = $_POST['paymentMode'] ?? 'Cash';
    $paymentStatus = $_POST['paymentStatus'] ?? 'Pending';
    $sets   = $_POST['sets'] ?? '';
    $statu   = $_POST['statu'] ?? '';

    // Check required fields
    if ($shopName == '' || $finalName == '' || $finalDate == '' || $finalTime == '') {
        $response['success'] = 0;
        $response['message'] = "Required fields are missing.";
    } else {
        $query = "INSERT INTO appoinment 
                  (shopname, shopaddress, shopemail, finalname, finalemail, finaldate, finaltime, finalservice, totalamount, paymentMode, paymentStatus,setno,statu) 
                  VALUES 
                  ('$shopName', '$shopAddress', '$shopEmail', '$finalName', '$finalEmail', '$finalDate', '$finalTime', '$finalService', '$totalAmount', '$paymentMode', '$paymentStatus', '$sets', '$statu')";

        if (mysqli_query($conn, $query)) {
            $response['success'] = 1;
            $response['message'] = "Booking saved successfully.";
        } else {
            $response['success'] = 0;
            $response['message'] = "Database error: " . mysqli_error($conn);
        }
    }

} else {
    $response['success'] = 0;
    $response['message'] = "Invalid request method.";
}

// Output JSON and terminate
echo json_encode($response);
exit; // Prevent accidental extra output

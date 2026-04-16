<?php
require_once("databaseconnect.php");

// Get 'finalemail' and 'statu' from POST request
$finalemail = $_POST['finalemail'] ?? '';
$statu      = $_POST['statu'] ?? '';

$response = array();

if (!empty($finalemail) && !empty($statu)) {
    // Update query
    $sql = "UPDATE appoinment SET statu=? WHERE finalemail=?";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("ss", $statu, $finalemail);

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Booking status updated successfully";
    } else {
        $response['success'] = 0;
        $response['message'] = "Update failed";
    }
} else {
    $response['success'] = 0;
    $response['message'] = "Missing parameters";
}

// Return JSON
header('Content-Type: application/json; charset=UTF-8');
echo json_encode($response);
?>

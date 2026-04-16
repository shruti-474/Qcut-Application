<?php
require_once("databaseconnect.php");

// Get values from POST
$id            = $_POST['id'] ?? '';
$paymentStatus = $_POST['paymentStatus'] ?? '';
$paymentMode   = $_POST['paymentMode'] ?? '';

$response = array();

if (!empty($id) && !empty($paymentStatus) && !empty($paymentMode)) {
    // Update query
    $sql = "UPDATE appoinment SET paymentStatus=?, paymentMode=? WHERE id=?";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("ssi", $paymentStatus, $paymentMode, $id);

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Payment details updated successfully";
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

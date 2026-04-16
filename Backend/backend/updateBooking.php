<?php
require_once("databaseconnect.php");

// Get 'id' and 'statu' from POST request
$id     = $_POST['id'] ?? '';
$statu  = $_POST['statu'] ?? '';

$response = array();

if (!empty($id) && !empty($statu)) {
    // Update query
    $sql = "UPDATE booking SET statu=? WHERE id=?";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("si", $statu, $id);

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
echo json_encode($response);
?>

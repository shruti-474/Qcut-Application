<?php
require_once("databaseconnect.php"); // DB connection

header('Content-Type: application/json; charset=UTF-8');

// Get POST values safely
$email      = trim($_POST['email'] ?? '');
$status     = trim($_POST['status'] ?? '');
$totalsets  = trim($_POST['totalsets'] ?? '');

$response = array();

// Basic validation
if (empty($email) || empty($status) || empty($totalsets)) {
    $response['success'] = 0;
    $response['message'] = "Email, status and totalsets are required.";
    echo json_encode($response);
    exit;
}

// First check if email exists
$checkSql = "SELECT id FROM shopdata WHERE shopemail = ? LIMIT 1";
$checkStmt = $connection->prepare($checkSql);
$checkStmt->bind_param("s", $email);
$checkStmt->execute();
$checkResult = $checkStmt->get_result();

if ($checkResult->num_rows > 0) {
    // Update query
    $updateSql = "UPDATE shopdata SET status = ?, totalsets = ? WHERE shopemail = ?";
    $updateStmt = $connection->prepare($updateSql);
    $updateStmt->bind_param("sss", $status, $totalsets, $email);

    if ($updateStmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Shop updated successfully.";
    } else {
        $response['success'] = 0;
        $response['message'] = "Failed to update shop.";
    }
} else {
    $response['success'] = 0;
    $response['message'] = "Email not found in shopdata.";
}

echo json_encode($response);
?>

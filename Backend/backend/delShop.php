<?php
require_once("databaseconnect.php");

// Get 'id' from POST request
$email = $_POST['email'] ?? '';

$response = array();

// Validate ID
if(!empty($email)) {

    // Delete query
    $sql = "DELETE FROM shopdata WHERE shopemail='$email'";
    $result = mysqli_query($connection, $sql);

    if($result) {
        $response['status'] = 'success';
        $response['message'] = 'salon deleted successfully.';
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Failed to delete salon.';
    }

} else {
    $response['status'] = 'error';
    $response['message'] = 'ID is missing.';
}

// Output JSON response
header('Content-Type: application/json');
echo json_encode($response);

// Close the database connection
mysqli_close($connection);
?>

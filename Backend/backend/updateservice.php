<?php
require_once("databaseconnect.php");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $shopEmail = $_POST['shopemail'];
    $service   = trim($_POST['service']); // remove unwanted spaces

    // Remove starting comma if exists
    if (substr($service, 0, 1) === ',') {
        $service = substr($service, 1);
    }

    // Clean double commas (if multiple services are concatenated)
    $service = preg_replace('/,+/', ',', $service);

    $sql = "UPDATE shopdetails SET service=? WHERE shopemail=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ss", $service, $shopEmail);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Service updated successfully"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error updating service"]);
    }

    $stmt->close();
    $conn->close();
}
?>

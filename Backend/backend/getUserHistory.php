<?php
require_once("databaseconnect.php");

// Get 'finalemail' from POST request
$finalemail = $_POST['finalemail'] ?? '';

$data = array();

// Fetch records by finalemail
$sql = "SELECT * FROM history WHERE finalemail='$finalemail'";
$result = mysqli_query($connection, $sql);

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = array(
        'id'            => $row['id'],
        'shopname'      => $row['shopname'],
        'shopaddress'   => $row['shopaddress'],
        'shopemail'     => $row['shopemail'],
        'finalname'     => $row['finalname'],
        'finalemail'    => $row['finalemail'],
        'finaldate'     => $row['finaldate'],
        'finaltime'     => $row['finaltime'],
        'finalservice'  => $row['finalservice'],
        'totalamount'   => $row['totalamount'],
        'paymentMode'   => $row['paymentMode'],
        'paymentStatus' => $row['paymentStatus'],
        'Appoinmentstatus' => $row['Appoinmentstatus']
    );
}

// Return JSON
echo json_encode(array('getOrders' => $data));
?>

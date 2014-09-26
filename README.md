akt-notes
=========

CMPUT 301 A1


"To Do" app containing 2 views, 1 for the list of active items and 1 for the list of archived items. In both views, you can tap on the selected item to check off and uncheck your items. On the action bar of both views, there are options allowing you to:
	1. Create a popup containing a summary of how many active and archived items exist and how many of each are completed/incomplete
	2. Enter the respective view's "Edit Mode"
	3. Enter the "Email" view, containing a sectioned list containing both the active and archived items

In addition to these options, the view containing the active items has an option that allows you to add an item to the active items list.

Edit Mode:
	In this view, the user can select any number of items > 0 and either delete, or archive the selected items. The user will be asked to confirm his selection with a popup dialog indicating how many items were selected. If the user hasn't selected any items, a toast will pop up indicating so instead.

Email:
	In this view, the user can select any number of items > 0 and click the checkmark to indicate (s)he is done. The user will be asked to confirm his selection with a popup dialog indicating how many items were selected. If an email client is installed, the user will be asked to select one and will be redirected to that client with the body and subject filled out. If the user hasn't selected any items, a toast will pop up indicating so. 

Add Item:
	In this view, there will be a field for the user to enter their item and a button, "Done", which when clicked will add the item to the list and redirect the user back to the main screen.

During any of these 3 views, the user is able to press back and navigate back to the main view.


Resources

Action Bar icons were taken from: https://developer.android.com/design/downloads/index.html
Tutorials referenced during the development of this app include:
	Android tab views: //http://developer.android.com/training/implementing-navigation/lateral.html
	Android popup dialogs: //http://developer.android.com/guide/topics/ui/dialogs.html
	Android API reference: http://developer.android.com/reference/packages.html

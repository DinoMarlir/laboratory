---
sidebar_position: 2
---

# Using templates

### Creating a new template

To create a new template run `laboratory template create <name>`. (`<name>` is the name of the template)

### Adding templates to servers

To add an existing template to a server run `laboratory modify <server-name>`.

Enter the number `4` to add a template to the server.
![laboratory-modify-promt.png](/img/docs/laboratory-template-promt-4.png)

Enter the number of your template you want to add.
![laboratory-template-selection.png](/img/docs/laboratory-template-selection.png)

Now your template has been successfully added to the server.
![laboratory-template-added.png](/img/docs/laboratory-template-added.png)

### Remove a template from the server

To remove an existing template from a server run `laboratory modify <server-name>`.

Enter the number `5` to add a template to the server.
![laboratory-modify-promt.png](/img/docs/laboratory-template-promt-5.png)

Enter the number of your template you want to remove.
![laboratory-template-selection.png](/img/docs/laboratory-template-selection.png)

Now your template has been successfully removed from the server.
![laboratory-template-added.png](/img/docs/laboratory-template-added.png)

### Enable/disable templates

Laboratory automatically toggles using templates.
![server-modify-templates-using.png](/img/docs/server-modify-templates-using.png)
![server-modify-templates-notusing.png](/img/docs/server-modify-templates-notusing.png)

### Deleting a template

To delete an existing template run `laboratory template delete <name>`. (`<name>` is the name of the template)

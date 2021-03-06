# Photogran
Application Android conçue et développée par __Thibaut Martinot__

---

Photogran est une application Android permettant de visualiser des photos aléatoires ou des images portant sur un thème spécifique grâce à la fonction de recherche d'image. Toutes les images sont visibles sur le site [Unsplash](https://unsplash.com/) et sont utilisables gratuitement pour un usage commercial ou non.

Photogran utilise l'API d'Unsplash afin de récupérer les images: https://unsplash.com/developers

Photogran est disponible en français et en anglais.

--- 

Photogran est constituée de deux écrans: 

* Un écran d'accueil disposant d'un message d'accueil, d'une brève description de l'application, d'un bouton permettant d'accéder à la seconde activité et d'un autre bouton permettant d'ouvrir le site [Unsplash](https://unsplash.com/) dans un navigateur. Cette activité dispose d'un layout légèrement modifié lorsque le téléphone est en mode paysage afin d'éviter une superposition des éléments et de faire ressortir le bouton d'accès à la galerie grâce une couleur plus adaptée par rapport à sa position sur l'image de fond.

* Un écran nommé "Gallerie" qui permet d'afficher des photos aléatoires parmi une selection de photos de l'API unsplash qui change régulièrement.

---

Le menu de l'action bar dispose de deux boutons:

* Un bouton symbolisé par une loupe permettant d'accéder à la fonction de recherche d'image.

* Un bouton symbolisé par un rouage permettant d'accéder au réglage du nombre d'images à afficher dans la gallerie.

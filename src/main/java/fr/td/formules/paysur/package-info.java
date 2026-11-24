/**
 * ACL (Anti-Corruption Layer) entre PaySûr et le contexte Formules et abonnements.
 *
 * PaySûr impose son format (section 7 : « Ce que GripUp peut y changer : Rien »).
 * Tout ce qui est propre à PaySûr vit ici, et seulement ici :
 *   - ReponsePaySur : la réponse telle que PaySûr l'envoie (ses codes, ses centimes) ;
 *   - TraducteurPaySur : la traduction vers ResultatDePaiement, avec nos mots ;
 *   - CodePaySurInconnu : un code non prévu ne passe jamais en silence.
 *
 * Si PaySûr change de format demain, seul ce package change.
 */
package fr.td.formules.paysur;

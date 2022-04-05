# commande-v2

JSON To test the code in Postman:

{
    "reference":"cmd9",
    "totalPaye":100,
    "commandeItems":[
        {"produit":{"code":"p1"}, "qte":5, "prix":20},
        {"produit":{"code":"p1"}, "qte":9, "prix":40}
    ],
    "client":{"cin":"EE1234"},
    "paiements":[
        {"code":"payment1", "montant":200, "typePaiement":{"code":"cash"}},
        {"code":"payment2", "montant":200, "typePaiement":{"code":"cash"}}
    ]
}

NOTE: You need to first add a client and a payment type to your database before testing the save commande method.

describe('Heist List', () => {

  it('should display a list of heists', () => {

    cy.fixture('heist-list.json').as("heistListJSON");

    cy.server();

    cy.route('/heist/list', "@heistListJSON").as("heists");

    cy.visit('/');

    cy.contains('Heists')

    cy.wait('@heists');

    cy.get("nb-list-item").should("have.length", 2);

  });

  it('should display heist details', () => {


  });

  it('should display all members', () => {



  })
})

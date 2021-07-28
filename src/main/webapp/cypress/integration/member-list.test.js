describe('Member List', () => {

  it('should display a list of members', () => {

    cy.fixture('member-list.json').as("memberListJSON");

    cy.server();

    cy.route('/member/list', "@memberListJSON").as("members");

    cy.visit('/members');

    cy.contains('Members')

    cy.get('button').contains('New Member');

    cy.wait('@members');

    cy.get("nb-list-item").should("have.length", 3);

  });

})

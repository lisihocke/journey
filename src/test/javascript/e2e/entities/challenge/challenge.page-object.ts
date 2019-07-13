import { by, element, ElementFinder } from 'protractor';

export class ChallengeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-challenge div table .btn-danger'));
  title = element.all(by.css('jhi-challenge div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getText();
  }
}

export class ChallengeUpdatePage {
  pageTitle = element(by.id('jhi-challenge-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  tagInput = element(by.id('field_tag'));
  challengeDescriptionInput = element(by.id('field_challengeDescription'));
  hypothesisInput = element(by.id('field_hypothesis'));
  probeInput = element(by.id('field_probe'));
  pauseCriteriaInput = element(by.id('field_pauseCriteria'));
  exitCriteriaInput = element(by.id('field_exitCriteria'));
  influencesInput = element(by.id('field_influences'));
  notesInput = element(by.id('field_notes'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setTagInput(tag) {
    await this.tagInput.sendKeys(tag);
  }

  async getTagInput() {
    return await this.tagInput.getAttribute('value');
  }

  async setChallengeDescriptionInput(challengeDescription) {
    await this.challengeDescriptionInput.sendKeys(challengeDescription);
  }

  async getChallengeDescriptionInput() {
    return await this.challengeDescriptionInput.getAttribute('value');
  }

  async setHypothesisInput(hypothesis) {
    await this.hypothesisInput.sendKeys(hypothesis);
  }

  async getHypothesisInput() {
    return await this.hypothesisInput.getAttribute('value');
  }

  async setProbeInput(probe) {
    await this.probeInput.sendKeys(probe);
  }

  async getProbeInput() {
    return await this.probeInput.getAttribute('value');
  }

  async setPauseCriteriaInput(pauseCriteria) {
    await this.pauseCriteriaInput.sendKeys(pauseCriteria);
  }

  async getPauseCriteriaInput() {
    return await this.pauseCriteriaInput.getAttribute('value');
  }

  async setExitCriteriaInput(exitCriteria) {
    await this.exitCriteriaInput.sendKeys(exitCriteria);
  }

  async getExitCriteriaInput() {
    return await this.exitCriteriaInput.getAttribute('value');
  }

  async setInfluencesInput(influences) {
    await this.influencesInput.sendKeys(influences);
  }

  async getInfluencesInput() {
    return await this.influencesInput.getAttribute('value');
  }

  async setNotesInput(notes) {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput() {
    return await this.notesInput.getAttribute('value');
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ChallengeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-challenge-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-challenge'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

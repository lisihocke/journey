/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';
import { JournalEntryComponentsPage, JournalEntryDeleteDialog, JournalEntryUpdatePage } from './journal-entry.page-object';

const expect = chai.expect;

describe('JournalEntry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let journalEntryUpdatePage: JournalEntryUpdatePage;
  let journalEntryComponentsPage: JournalEntryComponentsPage;
  let journalEntryDeleteDialog: JournalEntryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.accountMenu), 5000);
  });

  it('should load JournalEntries', async () => {
    await navBarPage.goToEntity('journal-entry');
    journalEntryComponentsPage = new JournalEntryComponentsPage();
    await browser.wait(ec.visibilityOf(journalEntryComponentsPage.title), 5000);
    expect(await journalEntryComponentsPage.getTitle()).to.eq('Journal Entries');
  });

  it('should load create JournalEntry page', async () => {
    await journalEntryComponentsPage.clickOnCreateButton();
    journalEntryUpdatePage = new JournalEntryUpdatePage();
    expect(await journalEntryUpdatePage.getPageTitle()).to.eq('Create or edit a Journal Entry');
    await journalEntryUpdatePage.cancel();
  });

  it('should create and save JournalEntries', async () => {
    const nbButtonsBeforeCreate = await journalEntryComponentsPage.countDeleteButtons();

    await journalEntryComponentsPage.clickOnCreateButton();
    await promise.all([
      journalEntryUpdatePage.setDateInput('2000-12-31'),
      journalEntryUpdatePage.setTitleInput('title'),
      journalEntryUpdatePage.setDescriptionInput('description'),
      journalEntryUpdatePage.setChallengeInput('1: 1')
    ]);
    expect(await journalEntryUpdatePage.getDateInput()).to.eq('2000-12-31', 'Expected date value to be equals to 2000-12-31');
    expect(await journalEntryUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await journalEntryUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await journalEntryUpdatePage.getChallengeInput()).to.eq('1: 1', 'Expected Challenge value to be equals to 1: 1');
    await journalEntryUpdatePage.save();
    expect(await journalEntryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await journalEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last JournalEntry', async () => {
    const nbButtonsBeforeDelete = await journalEntryComponentsPage.countDeleteButtons();
    await journalEntryComponentsPage.clickOnLastDeleteButton();

    journalEntryDeleteDialog = new JournalEntryDeleteDialog();
    expect(await journalEntryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Journal Entry?');
    await journalEntryDeleteDialog.clickOnConfirmButton();

    expect(await journalEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

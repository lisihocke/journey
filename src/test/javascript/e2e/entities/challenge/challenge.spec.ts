/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';
import { ChallengeComponentsPage, ChallengeDeleteDialog, ChallengeUpdatePage } from './challenge.page-object';

const expect = chai.expect;

describe('Challenge e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let challengeUpdatePage: ChallengeUpdatePage;
  let challengeComponentsPage: ChallengeComponentsPage;
  let challengeDeleteDialog: ChallengeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.accountMenu), 5000);
  });

  it('should load Challenges', async () => {
    await navBarPage.goToEntity('challenge');
    challengeComponentsPage = new ChallengeComponentsPage();
    await browser.wait(ec.visibilityOf(challengeComponentsPage.title), 5000);
    expect(await challengeComponentsPage.getTitle()).to.eq('Challenges');
  });

  it('should load create Challenge page', async () => {
    await challengeComponentsPage.clickOnCreateButton();
    challengeUpdatePage = new ChallengeUpdatePage();
    expect(await challengeUpdatePage.getPageTitle()).to.eq('Create or edit a Challenge');
    await challengeUpdatePage.cancel();
  });

  it('should create and save Challenges', async () => {
    const nbButtonsBeforeCreate = await challengeComponentsPage.countDeleteButtons();

    await challengeComponentsPage.clickOnCreateButton();
    await promise.all([
      challengeUpdatePage.setTagInput('tag'),
      challengeUpdatePage.setChallengeDescriptionInput('challengeDescription'),
      challengeUpdatePage.setHypothesisInput('hypothesis'),
      challengeUpdatePage.setProbeInput('probe'),
      challengeUpdatePage.setPauseCriteriaInput('pauseCriteria'),
      challengeUpdatePage.setExitCriteriaInput('exitCriteria'),
      challengeUpdatePage.setInfluencesInput('influences'),
      challengeUpdatePage.setNotesInput('notes')
    ]);
    expect(await challengeUpdatePage.getTagInput()).to.eq('tag', 'Expected Tag value to be equals to tag');
    expect(await challengeUpdatePage.getChallengeDescriptionInput()).to.eq(
      'challengeDescription',
      'Expected Challenge Description value to be equals to challengeDescription'
    );
    expect(await challengeUpdatePage.getHypothesisInput()).to.eq('hypothesis', 'Expected Hypothesis value to be equals to hypothesis');
    expect(await challengeUpdatePage.getProbeInput()).to.eq('probe', 'Expected Probe value to be equals to probe');
    expect(await challengeUpdatePage.getPauseCriteriaInput()).to.eq(
      'pauseCriteria',
      'Expected Pause Criteria value to be equals to pauseCriteria'
    );
    expect(await challengeUpdatePage.getExitCriteriaInput()).to.eq(
      'exitCriteria',
      'Expected Exit Criteria value to be equals to exitCriteria'
    );
    expect(await challengeUpdatePage.getInfluencesInput()).to.eq('influences', 'Expected Influences value to be equals to influences');
    expect(await challengeUpdatePage.getNotesInput()).to.eq('notes', 'Expected Notes value to be equals to notes');
    await challengeUpdatePage.save();
    expect(await challengeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await challengeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Challenge', async () => {
    const nbButtonsBeforeDelete = await challengeComponentsPage.countDeleteButtons();
    await challengeComponentsPage.clickOnLastDeleteButton();

    challengeDeleteDialog = new ChallengeDeleteDialog();
    expect(await challengeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Challenge?');
    await challengeDeleteDialog.clickOnConfirmButton();

    expect(await challengeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

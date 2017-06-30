/**
 * @license
 * Copyright 2016 Google Inc. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.security.wycheproof;

import java.math.BigInteger;
import junit.framework.TestCase;

/**
 * Test BigInteger class.
 *
 * <p>This unit tests focuses on checking security relevant properties.
 */
public class BigIntegerTest extends TestCase {
  public static final BigInteger[] NONPRIMES =
      new BigInteger[] {
        // small non prime integers
        new BigInteger("-1"),
        new BigInteger("0"),
        new BigInteger("1"),
        // If p is prime then the Mersenne number 2^p-1 is pseudoprime for base 2.
        new BigInteger("147573952589676412927"),
        new BigInteger("2361183241434822606847"),
        // pseudoprime squares derived from Wiefrich primes
        new BigInteger("1194649"),
        new BigInteger("12327121"),
        // G. Jaeschke: "On strong pseudoprimes to several bases", Math o. comp. v.61, p 915-926
        new BigInteger("2152302898747"),
        new BigInteger("3474749660383"),
        new BigInteger("341550071728321"),
        new BigInteger("41234316135705689041"),
        new BigInteger("1553360566073143205541002401"),
        new BigInteger("56897193526942024370326972321"),
        // A list of strong pseudoprimes to 12 or more bases from
        // https://arxiv.org/pdf/1509.00864v1.pdf
        new BigInteger("360681321802296925566181"),
        new BigInteger("164280218643672633986221"),
        new BigInteger("318665857834031151167461"),
        new BigInteger("7395010240794120709381"),
        new BigInteger("2995741773170734841812261"),
        new BigInteger("667636712015520329618581"),
        new BigInteger("3317044064679887385961981"),
        new BigInteger("3110269097300703345712981"),
        new BigInteger("552727880697763694556181"),
        new BigInteger("3404730287403079539471001"),
        // Richarg G.E. Pinch, "Some primality testing algorithms"
        // Some composites that passed Maple V's primality test.
        new BigInteger("10710604680091"),
        new BigInteger("4498414682539051"),
        new BigInteger("6830509209595831"),
        // Composites that passed the primality test of Mathematica 2.0
        new BigInteger("38200901201"),
        new BigInteger("6646915915638769"),
        // Composites that passed Axioms primality tests
        new BigInteger("168790877523676911809192454171451"),
        new BigInteger("68528663395046912244223605902738356719751082784386681071"),
        // A composite q that was acceptied by Gnu Crypto. p = 2*q + 1 is prime and could have been
        // used to break the SRP with that library.
        // http://www.iacr.org/archive/pkc2005/33860010/33860010.pdf
        new BigInteger(
            "2338274894573145273314679073561004052325493799717332496500873981"
                + "9154269566267911565762670147721173495706686483597956042863296855"
                + "8985491020031718032728786934761830612407539788738389834804112831"
                + "0484933712924414264511799715503596253054638290097305254378560604"
                + "3457282155730383806702845548017315217454390994052035233808454053"
                + "2209678251"),
        // I. Damgard, P. Landrock, and C. Pomerance. "Average case error estimates for the strong
        // probable prime test." Math. of Comp. v.61 (203), pp. 177-194.
        //
        // This paper gives bounds for the number of bases necessary to distinguish composites from
        // primes assuming that the tested integer has been chosen at random.
        //
        // The result is sometimes misinterpreted and used for pseudo primality tests. There the
        // assumption of the paper may not be valid, especially if the integer to test has been
        // chosen by a potentially malicious party. The following pseudoprimes are 1024 to 1280 bits
        // long, pass the MR test for about 1/4 of all bases. They may expose pseudo primality tests
        // that misinterpret the paper above.
        new BigInteger(
            "1730626114143993906582329178627391355248485443639984363030847275"
                + "1308542667309368405802823431259718338553667079600118481458180717"
                + "8685660312964257923307841168376622412972295432300191118906455596"
                + "0636099366430317210651098229261736987868487865820945209431391380"
                + "09108180649097618810676094425505547347369635059151651"),
        new BigInteger(
            "1360998858923994584770803056737393786894832450662215840614559215"
                + "7078401469699701619693552331616875038702785662417391573713804038"
                + "4334162971247398689854706029275431407028615888533012918610480990"
                + "0223861487963796923928503972465761214360888693859314270808955367"
                + "40913457414488449790876563256100025424908640036566991"),
        new BigInteger(
            "3166883756083864374213797577792765404121225450334428749136082272"
                + "6277047033553982548405648549530365358167591776542577633638251648"
                + "4708794514891794859629545011811469601460428778510574606216627610"
                + "9305946545256861710197652701722202600572822026157108510694466031"
                + "21873496655525294839125742517721479483987924741221051"),
        new BigInteger(
            "1867856940026786421328012256561867196437042470176831233408635728"
                + "4012048133274069029642553787088950310027377534718962029124718226"
                + "6051117402306961193255486702389001315623336948708913938663766675"
                + "8226042898352219886123870222547007370332879734207273564946511954"
                + "31833673259400609717994958747847898007198993092012403"),
        new BigInteger(
            "1796390661263009677994016718416108866609292079197277387452323006"
                + "0275054640418696223480642324316099214952402651731083265854610369"
                + "8224730515713772933976457906751697355710699183284927973990745341"
                + "2311585789546147810071014069499060003398982340466066800822746698"
                + "64309065127941774208780103317444828873858305613764441"),
        new BigInteger(
            "1971945240855615239359385779848543278459852521629467703761094672"
                + "8118959477624582999696057042356627216821194209090396598966531204"
                + "6718418693934025583647341695134065566474352554207810854787929284"
                + "0150420997586371167811999782663434687606795518115913678131471778"
                + "75984898571603525952885934025286780854976730638309011"),
        new BigInteger(
            "3150008132483461686934076250569912489352161839369648385736587811"
                + "5997183822673267428826594197361084397177321281419482762875475084"
                + "9549588421903702315436565970387119142993552663715551586581960248"
                + "0385985798377103010348982470471826679560834315129605222126122792"
                + "66245495948831504762364224662571600318327270033084451"),
        new BigInteger(
            "1708946102366142320715649564941267111465045510329061281476212740"
                + "3257341007679575909346478379354045686824680545379443269343860874"
                + "7656833753674500442989626468892341565281803639470271121897646438"
                + "0297951614184327979794615696929197488480659590917557764806142256"
                + "64177795195470929762281195447906351417368682849843661"),
        new BigInteger(
            "3440003701993474165051634746793659123122285886429390887874425798"
                + "0539628797086399700050400512078053335053587477375429177907276765"
                + "7299212000552180980839328379536622696237474649639004862671352175"
                + "9289352494669383679530748723706821659664185661303953233767365167"
                + "67872755699135674189217223109386132423577475778316621"),
        new BigInteger(
            "1384489093437226718803143517408309162781203263729355872818628349"
                + "4469526441585078582037579393159945688413133085030944516305313234"
                + "8118439725196722488178936032989162127096990828542718648126854836"
                + "3215014994571003679167792328172868632219526006267874696849394364"
                + "51238457213158274397103569820623680840515654121967511"),
        new BigInteger(
            "3164393868232068713466361644489248900506521258778648162638744470"
                + "1911965299134147189251276735683301303659631352068168884603764242"
                + "2058303711027020977731971469313828021385806157441077657252831199"
                + "5151267291454080409585783805054468065763411635886980943812129069"
                + "12979884469786023782783106107762632814558326808787771"),
        new BigInteger(
            "3429610981008641614862240834440649965306153336087134811684334214"
                + "2781784784880823158748073620472850986640780368357758806487624898"
                + "6627917970721407574366482474956464008840562218539437088991213443"
                + "6865812444686702307687466249240837612849694711488568460832793683"
                + "75653516879275492605255161494494045951397742913942521"),
        new BigInteger(
            "1452625971469501984029833170297844688666257429198467445330023676"
                + "9656278238192507513913993052034672663086092503841752084104847874"
                + "8173408690100803386628315407260460724324957380860416531476612359"
                + "3906755149555321240901275978836270398698785709389076413509994529"
                + "42901485886289548101644664410043634050840333618736241"),
        new BigInteger(
            "1038759703926769528247935492263828939187707228977771863002037516"
                + "6447956205097712397228992931571755867770661683705025240420111189"
                + "2868394359927108413461144788316226799790655169187714036057483534"
                + "1379449940364129845302840193911613461736295410032863048528135257"
                + "04436628490382188950208123780350213139500089456366841"),
        new BigInteger(
            "2092626848347087607813788288260128832160456227715887480495928337"
                + "8143090879636401536763351823849725734769339835456348322174433133"
                + "2528919261931329808331753456631473557050588173643619875493912344"
                + "2621229276297788264420214803881217267094948976676445054939538411"
                + "22580436721054077839958796624587867034849970594728503"),
        new BigInteger(
            "2599619740922490310102276030628575944314398371981276430688469612"
                + "6677200542107317525516057185980658562393307608335402672015071146"
                + "4375869967746325500371685760342898099183114419807958654613613854"
                + "3992594086574371567802232690430810966098771990068625112489903295"
                + "89067604336669355585402887916288037943709029191674911"),
        new BigInteger(
            "1484944124031033226177904474729672080414277081288126930486713099"
                + "2951571588154552341076434413035940622712601649739964234772603407"
                + "4857469854866669198948655053082186111410005989963123962723450119"
                + "6642285521712848015932226566546835102733706632115364632204156914"
                + "64644614536183219912321808854090915103775109695786903"),
        new BigInteger(
            "1989518237873611249304118136102198299910749751063921465712157675"
                + "0160983808717982713586255483659294934004326549705203383719900307"
                + "1263871954417468537542759843054899935687365194283002403584007149"
                + "4878371117610681917576307737065411378178051858121032451282900716"
                + "73542006653340382017735899123084476453857081318545991"),
        new BigInteger(
            "1001516549472529960430483041632117547467775871427023066924341669"
                + "8403875533009883195522874469432253701609651260735835128759135299"
                + "5655554893223580483857764100115214746205606713424344869011314868"
                + "3107942731565140938612794494135227597999675233998980692687287393"
                + "67463341929936509718884001903262839905032784262429503"),
        new BigInteger(
            "9523120659259647017050060990584944204556454726505878376759616021"
                + "0162649328723702952067039028722189901551617226203049071432575686"
                + "9121080710327716161595367835528315683397536615084723082551568364"
                + "5739952184995573677453019127607472426963971562388390300785220049"
                + "6655144350028488056721467755918814393298596327869403"),
        new BigInteger(
            "1132692896131390459872218381686704761578367301590229517271338668"
                + "7180351798454882497438382954425835621126393661367213143371283272"
                + "0317909560575328664237540188143415517690966913820407105917871661"
                + "6365405171372246705778112163771743546945868623307467307997875571"
                + "40869529264839386342569691698111927128704157615097153"),
        new BigInteger(
            "1468478445343635885853097900396288249751127795088868633754583040"
                + "9902362001013473990040942210829761549194134324343879441927807081"
                + "4832779046634885071582835688508933384516245429006116427382617949"
                + "0797131644177507421862332328509904031871287120929769029086074503"
                + "38866991708762791875618755240139448209180903665732503"),
        new BigInteger(
            "3584128102219288461651881096063603237206550481422480899564693370"
                + "6989198816192527918220789937223699304075118319904981106473056074"
                + "6022073963857677172123650694187762839204698560127083582857191356"
                + "7619601857134928125369766912580367127840531257344393938629885107"
                + "16841734490107828355376447675347185589367615046902903"),
        new BigInteger(
            "1364132540730201251194341844655148002850736352953819312409033451"
                + "0581806956034928961765696532883678319811153534823893271895441595"
                + "1560405730847235775035340408484490592989244418679611824558459897"
                + "3970103405588145228028549820125837425523682281669049087481368021"
                + "33702260393866732616878693744975284760231993354960641"),
        new BigInteger(
            "1201213090774136737579735845388953400530787071160074364126728551"
                + "5147303301856817241515212573079940841340044269783294804816431383"
                + "8284868252529547559069691488487393115675459439240223181642866729"
                + "3429931964973711015111163544551414299071832349845971998375812575"
                + "43510797169400923829329952407436122292964382266031253"),
        new BigInteger(
            "2749611624987217008377958765718501619519235049877079452181305657"
                + "4724564026272242180811435338643866876903591496649439319889570905"
                + "0358277453956447636555409300854018401232502616530331143658908539"
                + "1682266093367017969369633046927346949982338985082398103545735993"
                + "26268702707614772584950389057955691219494031838859791"),
        new BigInteger(
            "2472982114742145360236684931488791833991329788857529670921519821"
                + "2275589064185672494327017904494686810840150314271250166999768972"
                + "5488277075239138647902134463451436706674384070937644414378922408"
                + "3974240802538174272534726968041801996779685771019246098603577098"
                + "48139330190576017582503777009836399771498662905893541"),
        new BigInteger(
            "1865347188865809981733701115448160790425846704558108501171803277"
                + "4616363375301457541345495992357828826645510919897778446537615423"
                + "0882378024111971510810465276396395657415178370245114122816237302"
                + "3283933030997110906700611097744954222271394790237582780817507949"
                + "50528224900273599212004329531461848157373189908642761"),
        new BigInteger(
            "2852220663043188088074403487569592065121097371561337653207919689"
                + "5806125544163953214258963319594971029379263400735515369955250013"
                + "4028759770788754166339078427645792536001991144766729628117371050"
                + "3065276544543379585207323093069812639902415298676791716773723086"
                + "01834079961869281917489638496882679996929360595283091"),
        new BigInteger(
            "1812266945737014247808099956042081894034544124660090203840602123"
                + "8933795306490611245152382127750687774085253618514158494675518742"
                + "0904863344271961110077635791220319963938065397633608138595169161"
                + "4365952732191284608496816156732317034130054052863296504407952482"
                + "70402413500178325587999299003705988651612697260381081"),
        new BigInteger(
            "3461478020849089881636057301867459968070516872602987322072552610"
                + "6122696717667098851127207650635960731479969936151065921604956606"
                + "9966154794154897232197433347544538725616640069913605417680979153"
                + "7941663920296351630552463916932109774095427601318332413237900565"
                + "33380330505490643091283956267491266793755039018149751"),
        new BigInteger(
            "2919979800970068658905539811038971051531482233418344434620488229"
                + "7069955478191515351496193695554391114007400265274653645749899336"
                + "1682279972987736655018586409178146143048864596533190807270958265"
                + "8386345811616055097769189859096393752934393213978617302764485594"
                + "27971296399182413971389773786687947710326691137231821"),
        new BigInteger(
            "4145154657949019520090421067892641813006171355169512965016842717"
                + "9656056083456770700476740539321891137934371895885040718614132068"
                + "7659642286859932894319946334724710913927915623235424403674960713"
                + "3015105499354740364264523832014989327244648027567866644179973588"
                + "6851400410006028767095438327490143741004842299878541824964849824"
                + "9350463409537126165182086675091928210325509674933575383432172361"
                + "1"),
        new BigInteger(
            "7263570684005586918409651676342477962666063384721447164820162267"
                + "3637900031379949446961412145104626127789865770334124795558653625"
                + "0265693033953361779942717984970881106667840356057452866357943677"
                + "0299187611159071848712668510417692823114550446719084221850233353"
                + "5448622581084864206413295247972240845959033925761759264503271102"
                + "8711358281311325650083539770515252900036932499310037595977096965"
                + "3"),
        new BigInteger(
            "3266663369261434628530741133725226939380890029427130078147736465"
                + "2996576096098663002890375510448427552966457154556853852292297530"
                + "3748877081504218804805934729044054195328441392083622877088337833"
                + "3839361634881282676119589334709165541036798092721269225600972531"
                + "5718770210834831734568895865334347621480511738343920340920819445"
                + "8602267925535790135884214787193459118783550610652631217006506945"
                + "3"),
        new BigInteger(
            "2714275054823322283846947242758076394288969833351667671546539844"
                + "8651643040817257191932781992745878593029648459331742640462067216"
                + "1697889841582474665251728415469605782526629276939159099241968231"
                + "4563946417035619325266947257091436696492494177352417584114484475"
                + "6610785746958696759938071286674788996869751533043564234638049802"
                + "5949514917474176492949501669603402532532836245346872590582496734"
                + "1"),
        new BigInteger(
            "9266983839147571189339684823228253420616685503762301572754412275"
                + "1127843293258964600702452397526178329259267484695086911638972315"
                + "4961995491900251718105624646921122900256992784437600453557273835"
                + "3861591882136393291951512396743267427648075154617935904316277726"
                + "4493865072538820667287351636308197262473273185496543102226470825"
                + "6662877019047683511136380480413028925833068997034750517865449915"
                + "3"),
        new BigInteger(
            "6943685859962113005380005918916483733270787990846044283782171250"
                + "1096851232736318325302320935157976786131666696364333532268184395"
                + "8665950723719162662956110077840013184726020313880442980629145593"
                + "2383952066228056762115442513166346145910224582828425403465971492"
                + "7260150227126350939033438077449255619231502836209591891037493966"
                + "9366963730251619187615962926310287121840421140621429191345783703"
                + "1"),
        new BigInteger(
            "8697498751458522624235585703214358189002281420472600166267178848"
                + "9809876665869248037334395420081653868096056103537493131465377226"
                + "0261527380426559408040456405219848362023445401145511114936506394"
                + "6240472545398288332411818055040869427951279027405975059749360297"
                + "5868667379863439430711990295232153128900808256212596912819669440"
                + "8461633147852596585348243485566178333170181585431955731179108942"
                + "1"),
        new BigInteger(
            "4252141813347264797276096871298065242070358505647997888003056375"
                + "0053145248543520793105289129514510723624029989029530989695904117"
                + "1040883775014117200916554841688641022136017868157290925811239787"
                + "3664014299809018676076741787758620956387796785085878200748397774"
                + "1000982778291877557074489243525397961477916369196578483491169386"
                + "2005017885694815977695115290403992818456642027646266206320076800"
                + "3"),
        new BigInteger(
            "3356647398302910967427184201762737607743709740247537918189248572"
                + "1755339542838026890625250818518527348898974773340867800840791470"
                + "3182072707032492007084043715576322632015800128489921814932124611"
                + "2300036417841991332124497348096579076242448857352000771247540736"
                + "1254949324799638105796121388483225300620211599611028678202393567"
                + "8095635541751658983704832859578366318530922805928053806070359946"
                + "1"),
        new BigInteger(
            "3687125575229624930459923973437509789116885700012081850445193578"
                + "4486423768425089350932846437401841871613673810991112360643320652"
                + "0225782089986669258803208183278659152741552450801685341843048568"
                + "1125008729650000322084879372381541270180139663134377177885154388"
                + "9558281508372272794193460069397070875763691455289959733530070186"
                + "0360525342531389354962332229432027179470095997955180502375530175"
                + "3"),
        new BigInteger(
            "6088560764944279853795552546797623009536823218028533977947580608"
                + "2015023514992395624319060987001625566234648211868300524182306472"
                + "7026872385780533326083682960047644674758988927136149502626557718"
                + "2561494515318563339483672950711193945073071458843901227339418284"
                + "3084182493461642555372639047162641075337152622892862251731700231"
                + "6672547426448574167370804559242233207791080547486632014153528258"
                + "1"),
        new BigInteger(
            "6081766267938901970682791123502557184373300073506187731779846380"
                + "4083193288475152882563422230130613717216469536080649276981712202"
                + "8895707724700107471391574673087393411637415445150131834443587280"
                + "4243131225731630921851260690656608624225797294197741881140769183"
                + "0474230365914155543155728342161658185646742117306992051014301383"
                + "8040790841165308968256142923362988225719340573836171022480960175"
                + "3"),
        new BigInteger(
            "5000879164859908887599881582814678322611278734193303790540199067"
                + "6946804876309491380295619810590406766227048051997595053442615914"
                + "2677702465219525342790323338314172797422380779526016709102650201"
                + "6058553529785304239405043134061167377896985489252957729348463979"
                + "6066933483020351454994081895283883114206539208013308687170796286"
                + "8268752695525736731805658439398227370325903928664994918343915975"
                + "3"),
        new BigInteger(
            "5033660524073962747735388920230339629907033897027296197974576730"
                + "5654422857706257645566447425553553516325935964234373316093019314"
                + "9125347663842235853602126989737378697014753612431648011618832921"
                + "3881347704723060451053853932258315572365588301231482715348124261"
                + "9244292433503547002496906691530720882968955274299548904791181715"
                + "4190217453383503386985567684902027707247767653326442247588135031"
                + "1"),
        new BigInteger(
            "7656692474335471169217545128846872228367812415627865759181393395"
                + "8241618825828371173259648247363305034462523584509943049089351382"
                + "5433879053475325827330864407124355159557574919864955631956757119"
                + "9044097578241324151657090338112756973390540016908863938814598847"
                + "5396824620980380695503598397306050849284639259137342875066075738"
                + "4060141031090753207116787791385102134328808638802350088507846845"
                + "1"),
        new BigInteger(
            "6916552449605114038869281931877716049700938960849845402874660241"
                + "3416574115770967736955518993475194932835724065347138687353620520"
                + "8331524408574865319265572656228099650675609929009824224767253657"
                + "5459382651810818256784829505942965567114371502731619372511210627"
                + "1610362520274480185346478545007812850057950851029848830710521787"
                + "8356932374617076535457900683468313279449103472700012568700849319"
                + "1"),
        // F. Arnault, "Rabin-Miller primality test: composite numbers which pass it", Math. comp.
        // v.64, n.209, p 355-361.
        //
        // A strong pseudoprime for the first 46 primes
        new BigInteger(
            "8038374574536394912570796143419421081388376882875581458374889175"
                + "2229742737653336521865023361639600454579150420236032087665699667"
                + "6098728404396540823292873879185086916685732826776177102938969773"
                + "9470167082304286871099974399765441448453411558724506334092790222"
                + "7529622941498423068816854043264575340183297861112989606448452161"
                + "91652872597534901"),
        // Richard G.E. Pinch, Absolute quadratic pseudorprimes
        // http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.210.6783&rep=rep1&type=pdf
        // Lucas-Charmichael-(-) numbers
        new BigInteger("28295303263921"),
        new BigInteger("443372888629441"),
        new BigInteger("582920080863121"),
        new BigInteger("894221105778001"),
        new BigInteger("2013745337604001"),
        // Lucas-Charmichael-(+) numbers
        new BigInteger("6479"),
        new BigInteger("84419"),
        new BigInteger("1930499"),
        new BigInteger("7110179"),
        new BigInteger("15857855"),
        new BigInteger("63278892599"),
        new BigInteger("79397009999"),
      };

  /**
   * Tests BigInteger.isProbablePrime with a list of composite integers. The integers have been
   * chosen to check for weak pseudoprimality tests. E.g., they are counterexamples to weak
   * implementations. The implementation in jdk uses a combination of a Miller-Rabin test and a
   * Lucas test. This is similar to the Baillie-PSW test
   * https://en.wikipedia.org/wiki/Baillie%E2%80%93PSW_primality_test
   */
  public void testIsProbablePrime() throws Exception {
    // The probability that a non-prime passes should be at most 1-2^{-certainty}.
    int certainty = 80;
    for (BigInteger n : NONPRIMES) {
      if (n.isProbablePrime(certainty)) {
        fail("Composite number " + n.toString() + " passed as probable prime test");
      }
    }
  }
}

# train-loader-jb

Далее описано условие задачи с добавленными мною ограничениями на входные данные

Представьте себя грузчиком, работающим на товарной станции. В течении дня на станцию прибывают поезда и их надо немедленно разгружать.

Для каждого поезда известно:

номер поезда
время прибытия,
время, которое у вас займёт разгрузка,
сумма, которую вам заплатят за разгрузку этого поезда.
Начав разгрузку поезда, вы должны ее закончить и не можете разгружать два поезда одновременно.

Необязательно браться за разгрузку всех поездов. На станции есть другие грузчики.

Ваша задача написать алгоритм, который по этим исходным данным максимизирует ваш заработок.

Очень желательны тесты. Было бы прекрасно также получить несколько слов о реализованном алгоритме, его источнике, достоинствах, ограничениях, вылщедтчислительной сложности и тп.

# Входные данные:

Сначала идёт число n - количество поездов, неотрицательное число, помещающееся в int

Далее n строк, каждая из которых имеет вид (trainId, timeStart, duration, payment), где

trainId - номер поезда, неотрицательное число, помещающееся в int

timeStart - время прибытия поезда, это строка, в которой записано время согласно форматной строке из аргументов запуска (см. примеры запуска)

duration - время, которое займёт разгрузка, записано аналогично timeStart

payment - объём награды за разгрузку поезда, положительное число помещающееся в int

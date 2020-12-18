
class N
  attr_accessor :val

  def initialize(v)
    @val = v
  end

  def +(other)
    self.class.new(val + other.val)
  end

  def -(other)
    self.class.new(val * other.val)
  end

  def to_s
    val.to_s
  end
end

def parse(expr)
  eval(expr.gsub(/\*/, "-").gsub(/(\d+)/, 'N.new(\1)')).val
end

puts $stdin.map { |line| parse(line) }.inject(&:+)

